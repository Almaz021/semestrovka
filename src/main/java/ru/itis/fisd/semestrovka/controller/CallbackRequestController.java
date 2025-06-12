package ru.itis.fisd.semestrovka.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
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
            String errorMsg = bindingResult.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .reduce((s1, s2) -> s1 + "; " + s2)
                    .orElse("Invalid input");
            log.warn("Callback request validation failed: {}", errorMsg);
            return ResponseEntity.badRequest().body(errorMsg);
        }

        log.debug("Creating Callback Request with name {} and phone {}", request.getName(), request.getPhone());

        callbackRequestService.save(request.getName(), request.getPhone(), "NEW", LocalDateTime.now());

        log.debug("Callback Request successfully created");
        return ResponseEntity.ok().build();
    }

}
