package ru.itis.fisd.semestrovka.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.itis.fisd.semestrovka.entity.Apartment;
import ru.itis.fisd.semestrovka.entity.User;
import ru.itis.fisd.semestrovka.service.ApartmentService;
import ru.itis.fisd.semestrovka.service.UserService;

import java.util.Optional;

@Controller
@RequestMapping("/favorites")
@RequiredArgsConstructor
public class FavoritesController {

    private final UserService userService;
    private final ApartmentService apartmentService;

    @GetMapping
    public String showFavorites(Model model,
                                @AuthenticationPrincipal UserDetails userDetails,
                                @RequestParam(value = "page", defaultValue = "0") int page,
                                @RequestParam(value = "size", defaultValue = "5") int size) {

        User user = userService.findByUsername(userDetails.getUsername()).orElseThrow();

        Page<Apartment> favoritesPage = apartmentService.findFavoritesByUser(user, PageRequest.of(page, size));

        model.addAttribute("favorites", favoritesPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", favoritesPage.getTotalPages());
        model.addAttribute("size", size);

        return "favorites";
    }

    @PostMapping(value = "/{id}", headers = "X-Requested-With=XMLHttpRequest")
    @ResponseBody
    public ResponseEntity<?> addToFavorites(@AuthenticationPrincipal UserDetails userDetails,
                                            @PathVariable("id") Long id) {
        User user = userService.findByUsername(userDetails.getUsername()).orElseThrow();
        Apartment apartment = apartmentService.findByIdAvailable(id).orElseThrow();

        user.getFavoriteApartments().add(apartment);
        userService.save(user);

        return ResponseEntity.ok().build();
    }


    @PostMapping(value = "/{id}/remove", headers = "X-Requested-With=XMLHttpRequest")
    @ResponseBody
    public ResponseEntity<?> removeFromFavorites(@AuthenticationPrincipal UserDetails userDetails,
                                                 @PathVariable("id") Long id) {
        User user = userService.findByUsername(userDetails.getUsername()).orElseThrow();
        Apartment apartment = apartmentService.findByIdAvailable(id).orElseThrow();

        user.getFavoriteApartments().remove(apartment);
        userService.save(user);

        return ResponseEntity.ok().build();
    }

}
