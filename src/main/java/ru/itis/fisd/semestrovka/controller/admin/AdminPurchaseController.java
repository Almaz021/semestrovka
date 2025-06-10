package ru.itis.fisd.semestrovka.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.itis.fisd.semestrovka.service.PurchaseService;

@Controller
@RequestMapping("/admin/purchases")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminPurchaseController {

    private final PurchaseService purchaseService;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("purchases", purchaseService.findAll());
        return "admin/purchases/list";
    }
}
