package ru.itis.fisd.semestrovka.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.itis.fisd.semestrovka.entity.Apartment;
import ru.itis.fisd.semestrovka.entity.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface ApartmentRepository extends JpaRepository<Apartment, Long> {

    @Query("SELECT a FROM Apartment a WHERE a.status = 'AVAILABLE'")
    List<Apartment> findByStatusAvailable();


    @Query("SELECT a FROM Apartment a WHERE a.id = :id AND a.status = 'AVAILABLE'")
    Optional<Apartment> findByIdAndStatusAvailable(Long id);

    @Query("SELECT COUNT(p) > 0 FROM Purchase p WHERE p.apartment.id = :apartmentId AND p.user = :user")
    boolean hasUserPurchasedApartment(@Param("apartmentId") Long apartmentId,
                                      @Param("user") User user);

}
