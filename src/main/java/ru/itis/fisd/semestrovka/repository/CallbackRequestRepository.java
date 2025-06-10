package ru.itis.fisd.semestrovka.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.itis.fisd.semestrovka.entity.CallbackRequest;

import java.util.List;

@Repository
public interface CallbackRequestRepository extends JpaRepository<CallbackRequest, Long> {

    @Query("""
    SELECT c FROM CallbackRequest c
    ORDER BY
        CASE WHEN c.status = 'NEW' THEN 0 ELSE 1 END,
        c.requestedAt DESC
    """)
    List<CallbackRequest> findAllOrderByStatusAndDate();


}
