package ru.itis.fisd.semestrovka.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.itis.fisd.semestrovka.entity.User;
import ru.itis.fisd.semestrovka.entity.ViewingRequest;

import java.util.List;

@Repository
public interface ViewingRequestRepository extends JpaRepository<ViewingRequest, Long> {

    List<ViewingRequest> findAllByUser(User user);
}
