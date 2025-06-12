package ru.itis.fisd.semestrovka.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.itis.fisd.semestrovka.entity.orm.Apartment;
import ru.itis.fisd.semestrovka.entity.orm.Purchase;
import ru.itis.fisd.semestrovka.entity.orm.User;
import ru.itis.fisd.semestrovka.exception.ApartmentAlreadySoldException;
import ru.itis.fisd.semestrovka.exception.PurchaseNotFoundException;
import ru.itis.fisd.semestrovka.repository.PurchaseRepository;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class PurchaseService {

    private final PurchaseRepository purchaseRepository;
    private final ApartmentService apartmentService;


    @Transactional
    public void purchaseApartment(User user, Apartment apartment, String email) {
        log.debug("Purchase Apartment {}, User with id = {}. Email = {}", apartment, user.getId(), email);
        if (!apartment.getStatus().equals("AVAILABLE")) {
            throw new ApartmentAlreadySoldException(apartment.getId());
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
        log.debug("Find all purchases by user with id {}", user.getId());
        return purchaseRepository.findAllByUser(user, pageable);
    }


    public Page<Purchase> findAll(int page, int size) {
        log.debug("Find all purchases");
        Pageable pageable = PageRequest.of(page, size);
        return purchaseRepository.findAll(pageable);
    }

    public Purchase findById(Long purchaseId) {
        log.debug("Find purchase with id {}", purchaseId);
        return purchaseRepository.findById(purchaseId).orElseThrow(() -> new PurchaseNotFoundException(purchaseId));
    }
}
