package ru.itis.fisd.semestrovka.controller.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.itis.fisd.semestrovka.entity.dto.UserDto;
import ru.itis.fisd.semestrovka.service.UserService;

@Controller
@RequestMapping("admin/users")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
@Slf4j
public class AdminUserController {

    private final UserService userService;

    @GetMapping
    public String listUsers(Model model,
                            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
                            @RequestParam(value = "size", required = false, defaultValue = "10") int size) {

        log.debug("Prepare admin users list page");

        Page<UserDto> usersPage = userService.findAll(page, size);

        model.addAttribute("users", usersPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", usersPage.getTotalPages());
        model.addAttribute("size", size);

        log.debug("Show admin users list page");

        return "admin/users/list";
    }

    @PostMapping("/{id}/change-role")
    public String changeRole(@PathVariable Long id, @RequestParam String role) {
        log.debug("Handle admin user change role");
        userService.updateRole(id, role);
        return "redirect:/admin/users";
    }

}
