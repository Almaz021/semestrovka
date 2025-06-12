package ru.itis.fisd.semestrovka.controller.admin;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.itis.fisd.semestrovka.dto.request.ViewingRequestListRequest;
import ru.itis.fisd.semestrovka.entity.dto.ViewingRequestDto;
import ru.itis.fisd.semestrovka.service.ViewingRequestService;

@Controller
@RequestMapping("/admin/viewing-requests")
@RequiredArgsConstructor
@Slf4j
public class AdminViewingRequestController {

    private final ViewingRequestService viewingRequestService;

    @GetMapping
    public String list(@Valid ViewingRequestListRequest request,
                       BindingResult result,
                       Model model) {

        log.debug("Prepare admin viewing requests list page");

        if (result.hasErrors()) {
            model.addAttribute("errors", result.getAllErrors());
            log.info("There are validation errors in viewing requests list request");
            log.info("Errors: {}", result.getAllErrors());
            return "admin/viewing-requests/list";
        }

        Page<ViewingRequestDto> requestsPage = viewingRequestService.findAll(request.getPage(), request.getSize());

        model.addAttribute("viewingRequests", requestsPage.getContent());
        model.addAttribute("currentPage", request.getPage());
        model.addAttribute("totalPages", requestsPage.getTotalPages());
        model.addAttribute("size", request.getSize());

        log.debug("Show admin viewing requests list page");

        return "admin/viewing-requests/list";
    }
}
