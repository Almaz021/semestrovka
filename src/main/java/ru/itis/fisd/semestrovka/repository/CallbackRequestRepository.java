package ru.itis.fisd.semestrovka.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.itis.fisd.semestrovka.entity.CallbackRequest;

import java.util.List;

@Repository
public interface CallbackRequestRepository extends JpaRepository<CallbackRequest, Long> {

    @Query("SELECT c FROM CallbackRequest c ORDER BY c.status ASC, c.requestedAt DESC")
    Page<CallbackRequest> findAllOrderByStatusAndDate(Pageable pageable);


}
