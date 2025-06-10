package ru.itis.fisd.semestrovka.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.itis.fisd.semestrovka.entity.CallbackRequest;
import ru.itis.fisd.semestrovka.service.CallbackRequestService;

import java.time.LocalDateTime;

@Controller
@RequiredArgsConstructor
public class CallbackRequestController {

    private final CallbackRequestService callbackRequestService;

    @PostMapping("/callback")
    public ResponseEntity<String> handleCallback(@RequestParam("name") String name,
                                                 @RequestParam("phone") String phone) {

        CallbackRequest callbackRequest = CallbackRequest.builder()
                .name(name)
                .phone(phone)
                .status("NEW")
                .requestedAt(LocalDateTime.now()).build();

        callbackRequestService.save(callbackRequest);
        return ResponseEntity.ok().build();
    }
}
