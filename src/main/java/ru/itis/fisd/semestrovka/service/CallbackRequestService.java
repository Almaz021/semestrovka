package ru.itis.fisd.semestrovka.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.itis.fisd.semestrovka.entity.CallbackRequest;
import ru.itis.fisd.semestrovka.repository.CallbackRequestRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CallbackRequestService {

    private final CallbackRequestRepository callbackRequestRepository;

    public List<CallbackRequest> findAllOrderByStatusAndDate() {
        return callbackRequestRepository.findAllOrderByStatusAndDate();
    }

    public CallbackRequest save(CallbackRequest request) {
        return callbackRequestRepository.save(request);
    }

    public void markAsDone(Long id) {
        CallbackRequest request = callbackRequestRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Заявка не найдена"));
        request.setStatus("DONE");
        callbackRequestRepository.save(request);
    }

}
