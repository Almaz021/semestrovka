package ru.itis.fisd.semestrovka.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.itis.fisd.semestrovka.dto.request.PurchaseRequest;
import ru.itis.fisd.semestrovka.entity.dto.ApartmentDto;
import ru.itis.fisd.semestrovka.entity.dto.PurchaseDto;
import ru.itis.fisd.semestrovka.service.ApartmentService;
import ru.itis.fisd.semestrovka.service.PurchaseService;

@Controller
@RequestMapping("/purchase")
@RequiredArgsConstructor
@Slf4j
public class PurchaseController {

    private final ApartmentService apartmentService;
    private final PurchaseService purchaseService;

    @GetMapping("/{apartmentId}")
    public String purchaseForm(@PathVariable Long apartmentId, Model model) {

        log.debug("Prepare purchase form page");

        ApartmentDto apartment = apartmentService.findDtoByIdAvailable(apartmentId);

        model.addAttribute("apartment", apartment);
        model.addAttribute("purchaseRequestDto", new PurchaseRequest(""));


        log.debug("Show purchase form page");
        return "purchase_form";
    }

    @PostMapping("/{apartmentId}")
    public String confirmPurchase(@PathVariable Long apartmentId,
                                  @Valid @ModelAttribute("purchaseRequestDto") PurchaseRequest purchaseRequestDto,
                                  BindingResult bindingResult,
                                  @AuthenticationPrincipal UserDetails userDetails,
                                  Model model) {
        if (bindingResult.hasErrors()) {
            ApartmentDto apartment = apartmentService.findDtoByIdAvailable(apartmentId);
            model.addAttribute("apartment", apartment);
            log.info("There are validation errors in purchase request");
            log.info("Errors: {}", bindingResult.getAllErrors());
            return "purchase_form";
        }

        purchaseService.purchaseApartment(userDetails.getUsername(), apartmentId, purchaseRequestDto.email());

        return "redirect:/purchase/my";
    }

    @GetMapping("/my")
    public String purchasesPage(
            Model model,
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        log.debug("Prepare user purchases page");

        Page<PurchaseDto> purchasesPage = purchaseService.findAllByUsername(
                userDetails.getUsername(), PageRequest.of(page, size));

        model.addAttribute("purchasesPage", purchasesPage);

        log.debug("Show user purchases page");
        return "purchases";
    }

    @GetMapping("/my/{purchaseId}")
    public String viewMyPurchase(@PathVariable Long purchaseId,
                                 Model model,
                                 @AuthenticationPrincipal UserDetails userDetails) {
        log.debug("Handling user's purchase page");

        PurchaseDto purchase = purchaseService.getUserPurchase(purchaseId, userDetails.getUsername());
        ApartmentDto apartment = apartmentService.findDtoById(purchase.apartment().id());

        model.addAttribute("purchase", purchase);
        model.addAttribute("apartment", apartment);

        log.debug("Show user purchase page");
        return "apartment";
    }
}
