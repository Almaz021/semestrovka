package ru.itis.fisd.semestrovka.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.itis.fisd.semestrovka.entity.orm.Purchase;
import ru.itis.fisd.semestrovka.entity.orm.User;

@Repository
public interface PurchaseRepository extends JpaRepository<Purchase, Long> {
    Page<Purchase> findAllByUser(User user, Pageable pageable);

}
