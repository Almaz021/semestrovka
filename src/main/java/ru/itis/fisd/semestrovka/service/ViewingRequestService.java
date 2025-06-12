package ru.itis.fisd.semestrovka.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.itis.fisd.semestrovka.entity.orm.Apartment;
import ru.itis.fisd.semestrovka.entity.orm.User;
import ru.itis.fisd.semestrovka.entity.orm.ViewingRequest;
import ru.itis.fisd.semestrovka.exception.UserDoubleBookingException;
import ru.itis.fisd.semestrovka.exception.ViewingTimeConflictException;
import ru.itis.fisd.semestrovka.exception.ViewingTimeOutOfBoundsException;
import ru.itis.fisd.semestrovka.repository.ViewingRequestRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ViewingRequestService {

    private final ViewingRequestRepository viewingRequestRepository;

    public Page<ViewingRequest> findAllByUser(User user, Pageable pageable) {
        log.debug("Finding all viewing requests by user with id = {}", user.getId());
        return viewingRequestRepository.findAllByUser(user, pageable);
    }

    public void save(ViewingRequest request) {
        log.debug("Saving viewing request with id = {}", request.getId());
        LocalDateTime time = request.getPreferredDateTime();

        int hour = time.getHour();
        if (hour < 8 || hour >= 18) {
            throw new ViewingTimeOutOfBoundsException();
        }

        LocalDateTime end = time.plusHours(1);

        List<ViewingRequest> conflicts = viewingRequestRepository
                .findConflictingRequests(request.getApartment().getId(), time, end);

        if (!conflicts.isEmpty()) {
            throw new ViewingTimeConflictException(time);
        }

        List<ViewingRequest> userConflicts = viewingRequestRepository
                .findUserConflicts(request.getUser().getId(), time, end);

        if (!userConflicts.isEmpty()) {
            throw new UserDoubleBookingException(request.getUser().getId(), time);
        }

        viewingRequestRepository.save(request);
    }


    public Page<ViewingRequest> findAll(int page, int size) {
        log.debug("Finding all viewing requests");
        Pageable pageable = PageRequest.of(page, size);
        return viewingRequestRepository.findAll(pageable);
    }

    public List<LocalDateTime> getAvailableSlots(Apartment apartment) {
        log.debug("Get available slots of apartment {} for viewing request", apartment.getId());
        List<LocalDateTime> slots = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now().withMinute(0).withSecond(0).withNano(0);
        LocalDateTime endDate = now.plusDays(7);

        while (now.isBefore(endDate)) {
            int hour = now.getHour();
            if (hour >= 8 && hour <= 17) {
                LocalDateTime slotEnd = now.plusHours(1);
                boolean hasConflict = !viewingRequestRepository
                        .findConflictingRequests(apartment.getId(), now, slotEnd)
                        .isEmpty();
                if (!hasConflict) {
                    slots.add(now);
                }
            }
            now = now.plusHours(1);
        }

        return slots;
    }

}
