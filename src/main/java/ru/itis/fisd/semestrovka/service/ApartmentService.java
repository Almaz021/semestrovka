package ru.itis.fisd.semestrovka.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.itis.fisd.semestrovka.entity.dto.ApartmentDto;
import ru.itis.fisd.semestrovka.entity.orm.Apartment;
import ru.itis.fisd.semestrovka.entity.orm.User;
import ru.itis.fisd.semestrovka.exception.ApartmentNotFoundException;
import ru.itis.fisd.semestrovka.mapper.ApartmentMapper;
import ru.itis.fisd.semestrovka.repository.ApartmentRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class ApartmentService {

    private final ApartmentRepository apartmentRepository;
    private final ApartmentMapper apartmentMapper;

    public Page<ApartmentDto> findAll(int page, int size, String sort, String dir) {
        log.debug("Finding all apartments");

        Sort.Direction direction = "desc".equalsIgnoreCase(dir) ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sort));
        return apartmentRepository.findAll(pageable).map(apartmentMapper::toDto);
    }

    public Apartment findByIdAvailable(Long id) {
        log.debug("Finding available apartment by id {}", id);
        return apartmentRepository.findByIdAndStatusAvailable(id).orElseThrow(() -> new ApartmentNotFoundException(id));
    }

    public ApartmentDto findDtoByIdAvailable(Long id) {
        log.debug("Finding available apartment dto by id {}", id);
        Apartment apartment = findByIdAvailable(id);
        return apartmentMapper.toDto(apartment);
    }

    public Apartment findById(Long apartmentId) {
        log.debug("Finding apartment by id {}", apartmentId);
        return apartmentRepository.findById(apartmentId).orElseThrow(() -> new ApartmentNotFoundException(apartmentId));
    }

    public ApartmentDto findDtoById(Long apartmentId) {
        log.debug("Finding apartment dto by id {}", apartmentId);
        Apartment apartment = findById(apartmentId);
        return apartmentMapper.toDto(apartment);
    }

    public void save(Apartment apartment) {
        log.debug("Saving apartment {}", apartment);
        apartmentRepository.save(apartment);
    }

    public boolean hasUserPurchasedApartment(Long apartmentId, User user) {
        log.debug("Checking if user with id {} purchased apartment with id {}", user.getId(), apartmentId);

        return apartmentRepository.hasUserPurchasedApartment(apartmentId, user);
    }

    public void deleteById(Long id) {
        log.debug("Deleting apartment with id {}", id);
        apartmentRepository.deleteById(id);
    }

    public Page<ApartmentDto> findAvailableFiltered(Integer minPrice, Integer maxPrice, String sort, int page, int size) {
        log.debug("Finding available apartments with minPrice: {} and maxPrice: {} and sorting: {}", minPrice, maxPrice, sort);
        Sort sorting = Sort.unsorted();

        if ("asc".equals(sort)) {
            sorting = Sort.by(Sort.Direction.ASC, "price");
        } else if ("desc".equals(sort)) {
            sorting = Sort.by(Sort.Direction.DESC, "price");
        }

        Pageable pageable = PageRequest.of(page, size, sorting);
        return apartmentRepository.findByAvailableAndPriceBetween(minPrice, maxPrice, pageable).map(apartmentMapper::toDto);
    }

    public Page<Apartment> findFavoritesByUser(User user, Pageable pageable) {
        log.debug("Finding favorites apartment with user id {}", user.getId());
        return apartmentRepository.findAllByFavoriteByUser(user, pageable);
    }
}
