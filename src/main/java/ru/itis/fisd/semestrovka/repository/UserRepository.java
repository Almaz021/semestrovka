package ru.itis.fisd.semestrovka.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.itis.fisd.semestrovka.entity.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query(
            """
                    SELECT u
                    FROM User u
                    LEFT JOIN FETCH u.favoriteApartments
                    WHERE u.username = :username
                    """
    )
    Optional<User> findByUsername(@Param("username") String username);
}
