package ru.itis.fisd.semestrovka.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.itis.fisd.semestrovka.entity.orm.CallbackRequest;
import ru.itis.fisd.semestrovka.service.CallbackRequestService;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@Slf4j
public class CallbackRequestController {

    private final CallbackRequestService callbackRequestService;

    @PostMapping("/callback")
    public ResponseEntity<String> handleCallback(@RequestParam("name") String name,
                                                 @RequestParam("phone") String phone) {

        log.debug("Creating Callback Request with name {} and phone {}", name, phone);

        CallbackRequest callbackRequest = CallbackRequest.builder()
                .name(name)
                .phone(phone)
                .status("NEW")
                .requestedAt(LocalDateTime.now()).build();

        callbackRequestService.save(callbackRequest);

        log.debug("Callback Request successfully created");
        return ResponseEntity.ok().build();
    }
}
