package ru.itis.fisd.semestrovka.controller.admin;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.itis.fisd.semestrovka.dto.request.UserListRequest;
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
    public String listUsers(@Valid UserListRequest request,
                            BindingResult result,
                            Model model) {

        log.debug("Prepare admin users list page");

        if (result.hasErrors()) {
            model.addAttribute("errors", result.getAllErrors());
            return "admin/users/list";
        }

        Page<UserDto> usersPage = userService.findAll(request.getPage(), request.getSize());

        model.addAttribute("users", usersPage.getContent());
        model.addAttribute("currentPage", request.getPage());
        model.addAttribute("totalPages", usersPage.getTotalPages());
        model.addAttribute("size", request.getSize());

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
