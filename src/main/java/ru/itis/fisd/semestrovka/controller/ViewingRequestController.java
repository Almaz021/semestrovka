package ru.itis.fisd.semestrovka.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.itis.fisd.semestrovka.entity.Apartment;
import ru.itis.fisd.semestrovka.entity.User;
import ru.itis.fisd.semestrovka.entity.ViewingRequest;
import ru.itis.fisd.semestrovka.service.ApartmentService;
import ru.itis.fisd.semestrovka.service.UserService;
import ru.itis.fisd.semestrovka.service.ViewingRequestService;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/appointments")
@RequiredArgsConstructor
public class ViewingRequestController {

    private final UserService userService;
    private final ApartmentService apartmentService;
    private final ViewingRequestService viewingRequestService;


    @GetMapping
    public String appointments(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.findByUsername(userDetails.getUsername()).orElseThrow();
        List<ViewingRequest> requests = viewingRequestService.findAllByUser(user);
        model.addAttribute("requests", requests);
        return "appointments";
    }


    @GetMapping("/{apartmentId}")
    public String showForm(@PathVariable("apartmentId") Long apartmentId, Model model) {

        Apartment apartment = apartmentService.findById(apartmentId).orElseThrow();
        model.addAttribute("apartment", apartment);
        return "viewing_request_form";
    }

    @PostMapping("/{apartmentId}")
    public String submitRequest(@PathVariable Long apartmentId,
                                @RequestParam("preferredDateTime") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                                LocalDateTime preferredDateTime,
                                @AuthenticationPrincipal UserDetails userDetails) {

        User user = userService.findByUsername(userDetails.getUsername()).orElseThrow();
        Apartment apartment = apartmentService.findById(apartmentId).orElseThrow();

        ViewingRequest request = ViewingRequest.builder()
                .user(user)
                .apartment(apartment)
                .preferredDateTime(preferredDateTime)
                .build();

        viewingRequestService.save(request);
        return "redirect:/";
    }
}
