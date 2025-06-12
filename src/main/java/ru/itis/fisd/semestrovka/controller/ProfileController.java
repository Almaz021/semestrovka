package ru.itis.fisd.semestrovka.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.itis.fisd.semestrovka.dto.request.ProfileEditRequest;
import ru.itis.fisd.semestrovka.service.UserService;


@Controller
@RequestMapping("/profile")
@RequiredArgsConstructor
@Slf4j
public class ProfileController {

    private final UserService userService;

    @GetMapping
    public String profile(@AuthenticationPrincipal UserDetails userDetails, Model model) {

        log.debug("Prepare profile page");

        model.addAttribute("role", userDetails.getAuthorities());

        log.debug("Show profile page");

        return "profile";
    }

    @GetMapping("/edit")
    public String editProfileForm(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        log.debug("Prepare edit profile page");

        model.addAttribute("profileEditRequest", new ProfileEditRequest(userDetails.getUsername(), ""));

        log.debug("Show edit profile page");
        return "profile_edit";
    }

    @PostMapping("/edit")
    public String editProfileSubmit(@AuthenticationPrincipal UserDetails userDetails,
                                    @ModelAttribute ProfileEditRequest request) {
        log.debug("Handle edit profile request");

        userService.updateProfile(userDetails.getUsername(), request);

        log.debug("Edit profile completed");

        return "redirect:/profile";
    }

}
