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
import ru.itis.fisd.semestrovka.entity.orm.ViewingRequest;
import ru.itis.fisd.semestrovka.service.ViewingRequestService;

@Controller
@RequestMapping("/admin/viewing-requests")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
@Slf4j
public class AdminViewingRequestController {

    private final ViewingRequestService viewingRequestService;

    @GetMapping
    public String list(Model model,
                       @RequestParam(value = "page", required = false, defaultValue = "0") int page,
                       @RequestParam(value = "size", required = false, defaultValue = "10") int size) {

        log.debug("Prepare admin viewing requests list page");

        Page<ViewingRequest> requestsPage = viewingRequestService.findAll(page, size);

        model.addAttribute("viewingRequests", requestsPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", requestsPage.getTotalPages());
        model.addAttribute("size", size);

        log.debug("Show admin viewing requests list page");

        return "admin/viewing-requests/list";
    }
}
