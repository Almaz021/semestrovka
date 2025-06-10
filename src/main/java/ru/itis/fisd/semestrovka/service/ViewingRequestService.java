package ru.itis.fisd.semestrovka.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.itis.fisd.semestrovka.entity.Apartment;
import ru.itis.fisd.semestrovka.entity.User;
import ru.itis.fisd.semestrovka.entity.ViewingRequest;
import ru.itis.fisd.semestrovka.repository.ViewingRequestRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ViewingRequestService {

    private final ViewingRequestRepository viewingRequestRepository;


    public List<ViewingRequest> findAllByUser(User user) {
        return viewingRequestRepository.findAllByUser(user);
    }

    public void save(ViewingRequest request) {
        LocalDateTime time = request.getPreferredDateTime();

        int hour = time.getHour();
        if (hour < 8 || hour >= 18) {
            throw new IllegalArgumentException("Можно записываться только с 08:00 до 18:00.");
        }

        LocalDateTime end = time.plusHours(1);

        List<ViewingRequest> conflicts = viewingRequestRepository
                .findConflictingRequests(request.getApartment().getId(), time, end);

        if (!conflicts.isEmpty()) {
            throw new IllegalStateException("На это время уже есть запись.");
        }

        viewingRequestRepository.save(request);
    }


    public List<ViewingRequest> findAll() {
        return viewingRequestRepository.findAll();
    }

    public List<LocalDateTime> getAvailableSlots(Apartment apartment) {
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
