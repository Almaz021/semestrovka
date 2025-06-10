package ru.itis.fisd.semestrovka.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.itis.fisd.semestrovka.entity.Purchase;
import ru.itis.fisd.semestrovka.entity.User;

import java.util.List;

@Repository
public interface PurchaseRepository extends JpaRepository<Purchase, Integer> {
    Page<Purchase> findAllByUser(User user, Pageable pageable);
}
