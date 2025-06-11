package ru.itis.fisd.semestrovka.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Slf4j
public class RootController {

    @GetMapping("/")
    public String root() {
        log.debug("Show home page");
        return "redirect:/home";
    }
}
