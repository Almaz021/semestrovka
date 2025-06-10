package ru.itis.fisd.semestrovka.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.itis.fisd.semestrovka.service.UserService;

@Controller
@RequestMapping("admin/users")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminUserController {

    private final UserService userService;

    @GetMapping
    public String listUsers(Model model) {
        model.addAttribute("users", userService.findAll());
        return "admin/users/list";
    }

    @PostMapping("/{id}/change-role")
    public String changeRole(@PathVariable Long id, @RequestParam String role) {
        userService.updateRole(id, role);
        return "redirect:/admin/users";
    }

}
