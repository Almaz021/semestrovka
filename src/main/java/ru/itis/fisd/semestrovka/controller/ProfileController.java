package ru.itis.fisd.semestrovka.controller;

import lombok.RequiredArgsConstructor;
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
import ru.itis.fisd.semestrovka.entity.User;
import ru.itis.fisd.semestrovka.service.UserService;


@Controller
@RequestMapping("/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final UserService userService;
    private final BCryptPasswordEncoder passwordEncoder;
    private final UserDetailsService userDetailsService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping
    public String profile(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        model.addAttribute("role", userDetails.getAuthorities());

        return "profile";
    }


    @PreAuthorize("isAuthenticated()")
    @GetMapping("/edit")
    public String editProfileForm(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        model.addAttribute("profileEditRequest", new ProfileEditRequest(userDetails.getUsername(), ""));
        return "profile_edit";
    }


    @PostMapping("/edit")
    public String editProfileSubmit(@AuthenticationPrincipal UserDetails userDetails, @ModelAttribute ProfileEditRequest request) {
        User user = userService.findByUsername(userDetails.getUsername()).orElseThrow();

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

        return "redirect:/profile";
    }

}
