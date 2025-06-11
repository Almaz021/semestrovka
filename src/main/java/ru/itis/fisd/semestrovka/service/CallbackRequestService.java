package ru.itis.fisd.semestrovka.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.itis.fisd.semestrovka.entity.orm.CallbackRequest;
import ru.itis.fisd.semestrovka.repository.CallbackRequestRepository;

@Service
@RequiredArgsConstructor
public class CallbackRequestService {

    private final CallbackRequestRepository callbackRequestRepository;

    public Page<CallbackRequest> findAllOrderByStatusAndDate(Pageable pageable) {
        return callbackRequestRepository.findAllOrderByStatusAndDate(pageable);
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
