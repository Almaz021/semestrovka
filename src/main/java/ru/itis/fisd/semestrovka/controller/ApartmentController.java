package ru.itis.fisd.semestrovka.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    public String apartments(Model model,
                             @RequestParam(value = "minPrice", required = false, defaultValue = "0") Integer minPrice,
                             @RequestParam(value = "maxPrice", required = false, defaultValue = "10000") Integer maxPrice,
                             @RequestParam(value = "sort", required = false) String sort,
                             @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
                             @RequestParam(value = "size", required = false, defaultValue = "10") Integer size) {

        Page<Apartment> apartmentsPage = apartmentService.findAvailableFiltered(minPrice, maxPrice, sort, page, size);

        model.addAttribute("apartments", apartmentsPage.getContent());
        model.addAttribute("sort", sort);
        model.addAttribute("minPrice", minPrice);
        model.addAttribute("maxPrice", maxPrice);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", apartmentsPage.getTotalPages());

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
            boolean isFavorite = user.getFavoriteApartments().contains(apartment);
            model.addAttribute("isFavorite", isFavorite);
        } else {
            model.addAttribute("isFavorite", false);
        }

        return "apartment";
    }

    @GetMapping("/my/{id}")
    @PreAuthorize("isAuthenticated()")
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
