package ru.itis.fisd.semestrovka.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.itis.fisd.semestrovka.entity.CallbackRequest;
import ru.itis.fisd.semestrovka.repository.CallbackRequestRepository;

@Service
@RequiredArgsConstructor
public class CallbackRequestService {

    private final CallbackRequestRepository callbackRequestRepository;

    public CallbackRequest save(CallbackRequest request) {
        return callbackRequestRepository.save(request);
    }
}
