package ru.itis.fisd.semestrovka.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.itis.fisd.semestrovka.entity.User;
import ru.itis.fisd.semestrovka.entity.ViewingRequest;
import ru.itis.fisd.semestrovka.repository.ViewingRequestRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ViewingRequestService {

    private final ViewingRequestRepository viewingRequestRepository;


    public List<ViewingRequest> findAllByUser(User user) {
        return viewingRequestRepository.findAllByUser(user);
    }

    public void save(ViewingRequest request) {
        viewingRequestRepository.save(request);
    }
}
