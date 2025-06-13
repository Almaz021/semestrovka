package ru.itis.fisd.semestrovka.controller.admin;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.itis.fisd.semestrovka.dto.request.PurchaseListRequest;
import ru.itis.fisd.semestrovka.entity.dto.PurchaseDto;
import ru.itis.fisd.semestrovka.service.PurchaseService;

@Controller
@RequestMapping("/admin/purchases")
@RequiredArgsConstructor
@Slf4j
public class AdminPurchaseController {

    private final PurchaseService purchaseService;

    @GetMapping
    public String list(@Valid PurchaseListRequest request,
                       BindingResult result,
                       Model model) {

        log.debug("Prepare admin purchases list page");

        if (result.hasErrors()) {
            model.addAttribute("errors", result.getAllErrors());
            log.info("There are validation errors in purchase list request");
            log.info("Errors: {}", result.getAllErrors());
            return "admin/purchases/list";
        }

        Page<PurchaseDto> purchasesPage = purchaseService.findAll(request.getPage(), request.getSize());

        model.addAttribute("purchases", purchasesPage.getContent());
        model.addAttribute("currentPage", request.getPage());
        model.addAttribute("totalPages", purchasesPage.getTotalPages());
        model.addAttribute("size", request.getSize());

        return "admin/purchases/list";
    }
}
