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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.itis.fisd.semestrovka.dto.response.ViewingRequestFormDataResponse;
import ru.itis.fisd.semestrovka.entity.dto.ViewingRequestDto;
import ru.itis.fisd.semestrovka.entity.orm.Apartment;
import ru.itis.fisd.semestrovka.entity.orm.User;
import ru.itis.fisd.semestrovka.exception.UserDoubleBookingException;
import ru.itis.fisd.semestrovka.service.ApartmentService;
import ru.itis.fisd.semestrovka.service.UserService;
import ru.itis.fisd.semestrovka.service.ViewingRequestService;

import java.time.LocalDateTime;

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

        Page<ViewingRequestDto> requestsPage = viewingRequestService.findAllByUsername(
                userDetails.getUsername(), PageRequest.of(page, size)
        );

        model.addAttribute("requests", requestsPage);

        log.debug("Show appointments page");
        return "appointments";
    }



    @GetMapping("/{apartmentId}")
    public String showForm(@PathVariable("apartmentId") Long apartmentId, Model model) {
        log.debug("Prepare viewing request form page");

        ViewingRequestFormDataResponse formData = viewingRequestService.getViewingFormData(apartmentId);
        model.addAttribute("apartment", formData.apartment());
        model.addAttribute("availableSlots", formData.availableSlots());


        log.debug("Show viewing request form page");
        return "viewing_request_form";
    }


    @PostMapping("/{apartmentId}")
    public String submitRequest(@PathVariable Long apartmentId,
                                @RequestParam("preferredDateTime") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                                LocalDateTime preferredDateTime,
                                @AuthenticationPrincipal UserDetails userDetails,
                                RedirectAttributes redirectAttributes) {
        log.debug("Handle viewing request");
        User user = userService.findByUsername(userDetails.getUsername());
        Apartment apartment = apartmentService.findById(apartmentId);

        try {
            viewingRequestService.save(user, apartment, preferredDateTime);
            log.debug("Save viewing request");
            return "redirect:/appointments";
        } catch (UserDoubleBookingException e) {
            redirectAttributes.addFlashAttribute("error", "Нельзя бронировать две квартиры в одно время");
            return "redirect:/appointments/" + apartmentId;
        }
    }
}
