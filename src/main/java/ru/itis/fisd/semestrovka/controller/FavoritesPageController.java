package ru.itis.fisd.semestrovka.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/favorites")
@Slf4j
public class FavoritesPageController {
    @GetMapping
    public String favoritesPage() {
        log.debug("Show favorites page");
        return "favorites";
    }
}
