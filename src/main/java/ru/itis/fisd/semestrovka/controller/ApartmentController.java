package ru.itis.fisd.semestrovka.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import ru.itis.fisd.semestrovka.entity.dto.ApartmentDto;
import ru.itis.fisd.semestrovka.entity.orm.User;
import ru.itis.fisd.semestrovka.service.ApartmentService;
import ru.itis.fisd.semestrovka.service.UserService;

@Controller
@RequestMapping("/apartments")
@RequiredArgsConstructor
@Slf4j
public class ApartmentController {

    private final ApartmentService apartmentService;
    private final UserService userService;

    @GetMapping
    public String apartments(Model model,
                             @RequestParam(value = "minPrice", required = false, defaultValue = "0") Integer minPrice,
                             @RequestParam(value = "maxPrice", required = false, defaultValue = "10000") Integer maxPrice,
                             @RequestParam(value = "sort", required = false, defaultValue = "asc") String sort,
                             @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
                             @RequestParam(value = "size", required = false, defaultValue = "10") Integer size) {

        log.debug("Prepare apartments page");

        Page<ApartmentDto> apartmentsPage = apartmentService.findAvailableFiltered(minPrice, maxPrice, sort, page, size);

        model.addAttribute("apartments", apartmentsPage.getContent());
        model.addAttribute("sort", sort);
        model.addAttribute("minPrice", minPrice);
        model.addAttribute("maxPrice", maxPrice);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", apartmentsPage.getTotalPages());

        log.debug("Show apartments page");
        return "apartments";
    }


    @GetMapping("/{id}")
    public String getApartment(@PathVariable("id") Long id,
                               Model model,
                               @AuthenticationPrincipal UserDetails userDetails) {
        log.debug("Prepare apartment page with id = {}", id);

        ApartmentDto apartment = apartmentService.findDtoByIdAvailable(id);
        model.addAttribute("apartment", apartment);

        boolean isFavorite = userService.isApartmentFavoriteForUser(userDetails, apartment);
        model.addAttribute("isFavorite", isFavorite);

        log.debug("Show apartment page");
        return "apartment";
    }


    @GetMapping("/my/{id}")
    @PreAuthorize("isAuthenticated()")
    public String viewMyApartment(@PathVariable("id") Long id,
                                  Model model,
                                  @AuthenticationPrincipal UserDetails userDetails) {
        log.debug("Prepare purchased apartment page with id = {}", id);

        User user = userService.findByUsername(userDetails.getUsername());

        boolean hasPurchased = apartmentService.hasUserPurchasedApartment(id, user);

        if (!hasPurchased) {
            return "redirect:/home";
        }

        ApartmentDto apartment = apartmentService.findDtoById(id);
        model.addAttribute("apartment", apartment);

        log.debug("Show purchased apartment");

        return "apartment";
    }
}
