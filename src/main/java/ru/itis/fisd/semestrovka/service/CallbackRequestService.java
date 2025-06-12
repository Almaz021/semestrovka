package ru.itis.fisd.semestrovka.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.itis.fisd.semestrovka.entity.orm.CallbackRequest;
import ru.itis.fisd.semestrovka.exception.CallbackRequestNotFoundException;
import ru.itis.fisd.semestrovka.repository.CallbackRequestRepository;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class CallbackRequestService {

    private final CallbackRequestRepository callbackRequestRepository;

    public Page<CallbackRequest> findAllByStatusAndDate(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        log.debug("Find all order by status and date");
        return callbackRequestRepository.findAllByStatusAndDate(pageable);
    }

    public void save(String name, String phone, String status, LocalDateTime date) {
        log.debug("Save callback request with params name = {}, phone = {}, status = {} date = {}", name, phone, status, date);
        CallbackRequest request = CallbackRequest.builder()
                .name(name)
                .phone(phone)
                .status(status)
                .requestedAt(LocalDateTime.now()).build();
        callbackRequestRepository.save(request);
    }

    @Transactional
    public void markAsDone(Long id) {
        log.debug("Mark as done callback request with id = {}", id);
        CallbackRequest request = callbackRequestRepository.findById(id)
                .orElseThrow(() -> new CallbackRequestNotFoundException(id));
        request.setStatus("DONE");
        callbackRequestRepository.save(request);
    }

}
