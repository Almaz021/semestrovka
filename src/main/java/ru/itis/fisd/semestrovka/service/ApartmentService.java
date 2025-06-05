package ru.itis.fisd.semestrovka.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.itis.fisd.semestrovka.entity.Apartment;
import ru.itis.fisd.semestrovka.entity.User;
import ru.itis.fisd.semestrovka.repository.ApartmentRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ApartmentService {

    private final ApartmentRepository apartmentRepository;

    public List<Apartment> findAll() {
        return apartmentRepository.findAll();
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

}
