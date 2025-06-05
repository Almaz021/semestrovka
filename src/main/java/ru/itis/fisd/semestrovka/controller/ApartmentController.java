package ru.itis.fisd.semestrovka.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.itis.fisd.semestrovka.entity.Apartment;
import ru.itis.fisd.semestrovka.entity.User;
import ru.itis.fisd.semestrovka.service.ApartmentService;
import ru.itis.fisd.semestrovka.service.UserService;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/apartments")
@RequiredArgsConstructor
public class ApartmentController {

    private final ApartmentService apartmentService;
    private final UserService userService;

    @GetMapping
    public String apartments(Model model) {

        List<Apartment> apartments = apartmentService.findAllAvailable();

        model.addAttribute("apartments", apartments);
        model.addAttribute("sort", "sort");
        model.addAttribute("minPrice", 0);
        model.addAttribute("maxPrice", 100);


        return "apartments";
    }

    @GetMapping("/{id}")
    public String getApartment(@PathVariable("id") Long id,
                               Model model,
                               @AuthenticationPrincipal UserDetails userDetails) {
        Optional<Apartment> apartmentOptional = apartmentService.findByIdAvailable(id);

        if (apartmentOptional.isEmpty()) {
            return "redirect:/apartments";
        }

        Apartment apartment = apartmentOptional.get();
        model.addAttribute("apartment", apartment);

        if (userDetails != null) {
            User user = userService.findByUsername(userDetails.getUsername()).orElseThrow();
            System.out.println(user.getFavoriteApartments());
            System.out.println(apartment);
            boolean isFavorite = user.getFavoriteApartments().contains(apartment);
            model.addAttribute("isFavorite", isFavorite);
        } else {
            model.addAttribute("isFavorite", false);
        }

        return "apartment";
    }

    @GetMapping("/my/{id}")
    public String viewMyApartment(@PathVariable("id") Long id,
                                  Model model,
                                  @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return "redirect:/login";
        }

        User user = userService.findByUsername(userDetails.getUsername()).orElseThrow();

        boolean hasPurchased = apartmentService.hasUserPurchasedApartment(id, user);

        if (!hasPurchased) {
            return "redirect:/home";
        }

        Apartment apartment = apartmentService.findById(id).orElseThrow();
        model.addAttribute("apartment", apartment);

        return "apartment";
    }


}
