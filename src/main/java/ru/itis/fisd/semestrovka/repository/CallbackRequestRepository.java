package ru.itis.fisd.semestrovka.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.itis.fisd.semestrovka.entity.CallbackRequest;

@Repository
public interface CallbackRequestRepository extends JpaRepository<CallbackRequest, Long> {
}
