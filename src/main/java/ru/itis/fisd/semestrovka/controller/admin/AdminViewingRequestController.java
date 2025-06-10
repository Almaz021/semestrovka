package ru.itis.fisd.semestrovka.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.itis.fisd.semestrovka.service.ViewingRequestService;

@Controller
@RequestMapping("/admin/viewing-requests")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminViewingRequestController {

    private final ViewingRequestService viewingRequestService;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("viewingRequests", viewingRequestService.findAll());
        return "admin/viewing-requests/list";
    }
}
