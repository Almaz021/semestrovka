package ru.itis.fisd.semestrovka.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.itis.fisd.semestrovka.entity.User;
import ru.itis.fisd.semestrovka.entity.ViewingRequest;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ViewingRequestRepository extends JpaRepository<ViewingRequest, Long> {

    List<ViewingRequest> findAllByUser(User user);

    @Query("SELECT vr FROM ViewingRequest vr WHERE vr.apartment.id = :apartmentId " +
           "AND vr.preferredDateTime BETWEEN :start AND :end")
    List<ViewingRequest> findConflictingRequests(Long apartmentId, LocalDateTime start, LocalDateTime end);

}
