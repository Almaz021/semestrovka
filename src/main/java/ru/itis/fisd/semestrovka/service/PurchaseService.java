package ru.itis.fisd.semestrovka.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.itis.fisd.semestrovka.entity.Apartment;
import ru.itis.fisd.semestrovka.entity.Purchase;
import ru.itis.fisd.semestrovka.entity.User;
import ru.itis.fisd.semestrovka.repository.PurchaseRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PurchaseService {

    private final PurchaseRepository purchaseRepository;
    private final ApartmentService apartmentService;


    @Transactional
    public void purchaseApartment(User user, Apartment apartment, String comment) {
        if (!apartment.getStatus().equals("AVAILABLE")) {
            throw new IllegalStateException("Квартира уже продана");
        }

        apartment.setStatus("SOLD");
        apartmentService.save(apartment);

        Purchase purchase = Purchase.builder()
                .user(user)
                .apartment(apartment)
                .purchaseDate(LocalDateTime.now())
                .build();

        purchaseRepository.save(purchase);
    }

    public Page<Purchase> findAllByUser(User user, Pageable pageable) {
        return purchaseRepository.findAllByUser(user, pageable);
    }


    public Page<Purchase> findAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return purchaseRepository.findAll(pageable);
    }
}
