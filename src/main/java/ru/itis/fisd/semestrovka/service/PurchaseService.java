package ru.itis.fisd.semestrovka.service;

import lombok.RequiredArgsConstructor;
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
        if (!apartment.getStatus().equals("available")) {
            throw new IllegalStateException("Квартира уже продана");
        }

        apartment.setStatus("sold");
        apartmentService.save(apartment);

        Purchase purchase = Purchase.builder()
                .user(user)
                .apartment(apartment)
                .purchaseDate(LocalDateTime.now())
                .build();

        purchaseRepository.save(purchase);
    }

    public List<Purchase> findAllByUser(User user) {
        return purchaseRepository.findAllByUser(user);
    }
}
