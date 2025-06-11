package ru.itis.fisd.semestrovka.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.itis.fisd.semestrovka.entity.orm.Apartment;
import ru.itis.fisd.semestrovka.entity.orm.User;
import ru.itis.fisd.semestrovka.entity.orm.ViewingRequest;
import ru.itis.fisd.semestrovka.service.ApartmentService;
import ru.itis.fisd.semestrovka.service.UserService;
import ru.itis.fisd.semestrovka.service.ViewingRequestService;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/appointments")
@RequiredArgsConstructor
@Slf4j
public class ViewingRequestController {

    private final UserService userService;
    private final ApartmentService apartmentService;
    private final ViewingRequestService viewingRequestService;


    @GetMapping
    public String appointments(
            Model model,
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        log.debug("Prepare appointments page");

        User user = userService.findByUsername(userDetails.getUsername());
        Page<ViewingRequest> requestsPage = viewingRequestService.findAllByUser(user, PageRequest.of(page, size));

        model.addAttribute("requests", requestsPage);

        log.debug("Show appointments page");
        return "appointments";
    }



    @GetMapping("/{apartmentId}")
    public String showForm(@PathVariable("apartmentId") Long apartmentId, Model model) {
        log.debug("Prepare viewing request form page");
        Apartment apartment = apartmentService.findById(apartmentId);
        List<LocalDateTime> availableSlots = viewingRequestService.getAvailableSlots(apartment);

        model.addAttribute("apartment", apartment);
        model.addAttribute("availableSlots", availableSlots);

        log.debug("Show viewing request form page");
        return "viewing_request_form";
    }


    @PostMapping("/{apartmentId}")
    public String submitRequest(@PathVariable Long apartmentId,
                                @RequestParam("preferredDateTime") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                                LocalDateTime preferredDateTime,
                                @AuthenticationPrincipal UserDetails userDetails,
                                Model model) {
        log.debug("Handle viewing request");
        User user = userService.findByUsername(userDetails.getUsername());
        Apartment apartment = apartmentService.findById(apartmentId);

        ViewingRequest request = ViewingRequest.builder()
                .user(user)
                .apartment(apartment)
                .preferredDateTime(preferredDateTime)
                .build();

        try {
            viewingRequestService.save(request);
            log.debug("Save viewing request");
            return "redirect:/appointments";
        } catch (IllegalArgumentException | IllegalStateException e) {
            model.addAttribute("apartment", apartment);
            model.addAttribute("error", e.getMessage());
            log.info("Failed to save viewing request");
            return "viewing_request_form";
        }
    }

}