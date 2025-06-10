package ru.itis.fisd.semestrovka.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.itis.fisd.semestrovka.entity.Apartment;
import ru.itis.fisd.semestrovka.entity.User;
import ru.itis.fisd.semestrovka.repository.ApartmentRepository;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ApartmentService {

    private final ApartmentRepository apartmentRepository;

    public Page<Apartment> findAll(int page, int size, String sort, String dir) {
        Sort.Direction direction = "desc".equalsIgnoreCase(dir) ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sort));
        return apartmentRepository.findAll(pageable);
    }

    public List<Apartment> findAllAvailable() {
        return apartmentRepository.findByStatusAvailable();
    }

    public Optional<Apartment> findByIdAvailable(Long id) {
        return apartmentRepository.findByIdAndStatusAvailable(id);
    }

    public Optional<Apartment> findById(Long apartmentId) {
        return apartmentRepository.findById(apartmentId);
    }

    public void save(Apartment apartment) {
        apartmentRepository.save(apartment);
    }

    public boolean hasUserPurchasedApartment(Long apartmentId, User user) {
        return apartmentRepository.hasUserPurchasedApartment(apartmentId, user);
    }

    public void deleteById(Long id) {
        apartmentRepository.deleteById(id);
    }

    public Page<Apartment> findAvailableFiltered(Integer minPrice, Integer maxPrice, String sort, int page, int size) {
        Sort sorting = Sort.unsorted();

        if ("price_asc".equals(sort)) {
            sorting = Sort.by(Sort.Direction.ASC, "price");
        } else if ("price_desc".equals(sort)) {
            sorting = Sort.by(Sort.Direction.DESC, "price");
        }

        Pageable pageable = PageRequest.of(page, size, sorting);
        return apartmentRepository.findByAvailableAndPriceBetween(minPrice, maxPrice, pageable);
    }

    public Page<Apartment> findFavoritesByUser(User user, Pageable pageable) {
        return apartmentRepository.findAllByFavoriteByUser(user, pageable);
    }
}
