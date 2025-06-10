package ru.itis.fisd.semestrovka.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.itis.fisd.semestrovka.entity.Apartment;
import ru.itis.fisd.semestrovka.entity.Purchase;
import ru.itis.fisd.semestrovka.entity.User;
import ru.itis.fisd.semestrovka.service.ApartmentService;
import ru.itis.fisd.semestrovka.service.PurchaseService;
import ru.itis.fisd.semestrovka.service.UserService;

import java.util.List;

@Controller
@RequestMapping("/purchase")
@RequiredArgsConstructor
public class PurchaseController {

    private final ApartmentService apartmentService;
    private final UserService userService;
    private final PurchaseService purchaseService;

    @GetMapping("/{apartmentId}")
    @PreAuthorize("isAuthenticated()")
    public String purchaseForm(@PathVariable Long apartmentId, Model model) {

        Apartment apartment = apartmentService.findByIdAvailable(apartmentId).orElseThrow();

        model.addAttribute("apartment", apartment);
        return "purchase_form";
    }

    @PostMapping("/{apartmentId}")
    @PreAuthorize("isAuthenticated()")
    public String confirmPurchase(@PathVariable Long apartmentId,
                                  @RequestParam(required = false) String comment,
                                  @AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.findByUsername(userDetails.getUsername()).orElseThrow();
        Apartment apartment = apartmentService.findByIdAvailable(apartmentId).orElseThrow();

        purchaseService.purchaseApartment(user, apartment, comment);

        return "redirect:/purchase/my";
    }

    @GetMapping("/my")
    @PreAuthorize("isAuthenticated()")
    public String purchasesPage(
            Model model,
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        User user = userService.findByUsername(userDetails.getUsername()).orElseThrow();

        Page<Purchase> purchasesPage = purchaseService.findAllByUser(user, PageRequest.of(page, size));

        model.addAttribute("purchasesPage", purchasesPage);
        return "purchases";
    }

}
