package ru.itis.fisd.semestrovka.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.itis.fisd.semestrovka.entity.orm.Apartment;
import ru.itis.fisd.semestrovka.entity.orm.User;

import java.util.Optional;

@Repository
public interface ApartmentRepository extends JpaRepository<Apartment, Long> {

    @Query("""
            SELECT a
            FROM Apartment a
            WHERE a.id = :id AND a.status = 'AVAILABLE'
            """)
    Optional<Apartment> findByIdAndStatusAvailable(Long id);

    @Query("""
            SELECT COUNT(p) > 0
            FROM Purchase p
            WHERE p.apartment.id = :apartmentId AND p.user = :user
            """)
    boolean hasUserPurchasedApartment(@Param("apartmentId") Long apartmentId,
                                      @Param("user") User user);

    @Query("""
            SELECT a
            FROM Apartment a
            WHERE a.status = 'AVAILABLE' AND a.price BETWEEN :minPrice AND :maxPrice
            """)
    Page<Apartment> findByAvailableAndPriceBetween(Integer minPrice, Integer maxPrice, Pageable pageable);

    @Query("""
            SELECT a
            FROM Apartment a
            JOIN a.favoriteBy u
            WHERE u = :user AND a.status = 'AVAILABLE'
            """)
    Page<Apartment> findAllByFavoriteByUser(@Param("user") User user, Pageable pageable);

}
