package ru.itis.fisd.semestrovka.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.itis.fisd.semestrovka.dto.request.CallbackCreateRequest;
import ru.itis.fisd.semestrovka.service.CallbackRequestService;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@Slf4j
public class CallbackRequestController {

    private final CallbackRequestService callbackRequestService;

    @PostMapping("/callback")
    public ResponseEntity<String> handleCallback(@Valid CallbackCreateRequest request,
                                                 BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            log.info("There are validation errors in callback create request request");
            log.info("Errors: {}", bindingResult.getAllErrors());
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors().toString());
        }

        log.debug("Creating Callback Request with name {} and phone {}", request.getName(), request.getPhone());

        callbackRequestService.save(request.getName(), request.getPhone(), "NEW", LocalDateTime.now());

        log.debug("Callback Request successfully created");
        return ResponseEntity.ok().build();
    }

}
