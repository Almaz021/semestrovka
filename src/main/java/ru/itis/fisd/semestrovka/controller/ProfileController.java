package ru.itis.fisd.semestrovka.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.itis.fisd.semestrovka.dto.request.ProfileEditRequest;
import ru.itis.fisd.semestrovka.entity.orm.User;
import ru.itis.fisd.semestrovka.service.UserService;


@Controller
@RequestMapping("/profile")
@RequiredArgsConstructor
@Slf4j
public class ProfileController {

    private final UserService userService;
    private final BCryptPasswordEncoder passwordEncoder;
    private final UserDetailsService userDetailsService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping
    public String profile(@AuthenticationPrincipal UserDetails userDetails, Model model) {

        log.debug("Prepare profile page");

        model.addAttribute("role", userDetails.getAuthorities());

        log.debug("Show profile page");

        return "profile";
    }


    @PreAuthorize("isAuthenticated()")
    @GetMapping("/edit")
    public String editProfileForm(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        log.debug("Prepare edit profile page");

        model.addAttribute("profileEditRequest", new ProfileEditRequest(userDetails.getUsername(), ""));

        log.debug("Show edit profile page");
        return "profile_edit";
    }


    @PostMapping("/edit")
    public String editProfileSubmit(@AuthenticationPrincipal UserDetails userDetails, @ModelAttribute ProfileEditRequest request) {
        log.debug("Handle edit profile request");
        User user = userService.findByUsername(userDetails.getUsername());

        if (request.username() != null && !request.username().isEmpty()) {
            user.setUsername(request.username());
        }
        if (request.password() != null && !request.password().isEmpty()) {
            user.setPasswordHash(passwordEncoder.encode(request.password()));
        }
        userService.save(user);

        UserDetails updatedUserDetails = userDetailsService.loadUserByUsername(user.getUsername());

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(
                        updatedUserDetails,
                        updatedUserDetails.getPassword(),
                        updatedUserDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        log.debug("Edit profile completed");

        return "redirect:/profile";
    }

}
