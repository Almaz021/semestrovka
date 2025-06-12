package ru.itis.fisd.semestrovka.controller.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.itis.fisd.semestrovka.entity.dto.PurchaseDto;
import ru.itis.fisd.semestrovka.entity.orm.Purchase;
import ru.itis.fisd.semestrovka.service.PurchaseService;

@Controller
@RequestMapping("/admin/purchases")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
@Slf4j
public class AdminPurchaseController {

    private final PurchaseService purchaseService;

    @GetMapping
    public String list(Model model,
                       @RequestParam(value = "page", required = false, defaultValue = "0") int page,
                       @RequestParam(value = "size", required = false, defaultValue = "10") int size) {

        log.debug("Prepare admin purchases list page");

        Page<PurchaseDto> purchasesPage = purchaseService.findAll(page, size);

        model.addAttribute("purchases", purchasesPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", purchasesPage.getTotalPages());
        model.addAttribute("size", size);

        log.debug("Show admin purchases list page");

        return "admin/purchases/list";
    }
}
