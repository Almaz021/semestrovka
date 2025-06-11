package ru.itis.fisd.semestrovka.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.itis.fisd.semestrovka.entity.orm.Apartment;
import ru.itis.fisd.semestrovka.entity.orm.Purchase;
import ru.itis.fisd.semestrovka.entity.orm.User;
import ru.itis.fisd.semestrovka.service.ApartmentService;
import ru.itis.fisd.semestrovka.service.PurchaseService;
import ru.itis.fisd.semestrovka.service.UserService;

@Controller
@RequestMapping("/purchase")
@RequiredArgsConstructor
@Slf4j
public class PurchaseController {

    private final ApartmentService apartmentService;
    private final UserService userService;
    private final PurchaseService purchaseService;

    @GetMapping("/{apartmentId}")
    @PreAuthorize("isAuthenticated()")
    public String purchaseForm(@PathVariable Long apartmentId, Model model) {

        log.debug("Prepare purchase form page");

        Apartment apartment = apartmentService.findByIdAvailable(apartmentId);

        model.addAttribute("apartment", apartment);

        log.debug("Show purchase form page");
        return "purchase_form";
    }

    @PostMapping("/{apartmentId}")
    @PreAuthorize("isAuthenticated()")
    public String confirmPurchase(@PathVariable Long apartmentId,
                                  @RequestParam(required = false) String comment,
                                  @AuthenticationPrincipal UserDetails userDetails) {
        log.debug("Handling confirm purchase form");

        User user = userService.findByUsername(userDetails.getUsername());
        Apartment apartment = apartmentService.findByIdAvailable(apartmentId);

        purchaseService.purchaseApartment(user, apartment, comment);

        log.debug("Handled confirm purchase form");

        return "redirect:/purchase/my";
    }

    @GetMapping("/my")
    @PreAuthorize("isAuthenticated()")
    public String purchasesPage(
            Model model,
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        log.debug("Prepare user purchases page");

        User user = userService.findByUsername(userDetails.getUsername());

        Page<Purchase> purchasesPage = purchaseService.findAllByUser(user, PageRequest.of(page, size));

        model.addAttribute("purchasesPage", purchasesPage);

        log.debug("Show user purchases page");
        return "purchases";
    }

    @GetMapping("/my/{purchaseId}")
    @PreAuthorize("isAuthenticated()")
    public String viewMyPurchase(@PathVariable Long purchaseId,
                                 Model model,
                                 @AuthenticationPrincipal UserDetails userDetails) {
        log.debug("Handling users purchase page");

        User user = userService.findByUsername(userDetails.getUsername());
        Purchase purchase = purchaseService.findById(purchaseId);

        if (!purchase.getUser().getId().equals(user.getId())) {
            log.warn("Purchase is not owned by user");
            return "error/403";
        }

        model.addAttribute("purchase", purchase);
        model.addAttribute("apartment", purchase.getApartment());

        log.debug("Show user purchase page");
        return "apartment";
    }


}
