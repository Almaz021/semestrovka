package ru.itis.fisd.semestrovka.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import ru.itis.fisd.semestrovka.dto.request.RegisterRequest;
import ru.itis.fisd.semestrovka.service.UserService;

@Controller
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final UserService userService;

    @GetMapping("/login")
    public String login() {
        log.debug("Show login page");
        return "login";
    }

    @GetMapping("/register")
    public String registerForm() {
        log.debug("Show register page");
        return "register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute RegisterRequest request) {
        log.debug("Registering user");

        userService.save(request.username(), request.password());

        log.debug("User registered successfully");
        return "redirect:/login";
    }


    @GetMapping("/access-denied")
    public String accessDenied() {
        log.debug("Show access denied page");
        return "error/403";
    }
}
