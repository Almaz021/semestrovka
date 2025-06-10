package ru.itis.fisd.semestrovka.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.itis.fisd.semestrovka.entity.Apartment;
import ru.itis.fisd.semestrovka.service.ApartmentService;

@Controller
@RequestMapping("/admin/apartments")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminApartmentController {

    private final ApartmentService apartmentService;

    @GetMapping
    public String list(Model model,
                       @RequestParam(value = "page", required = false, defaultValue = "0") int page,
                       @RequestParam(value = "size", required = false, defaultValue = "10") int size,
                       @RequestParam(value = "sort", required = false, defaultValue = "id") String sort,
                       @RequestParam(value = "dir", required = false, defaultValue = "asc") String dir) {

        Page<Apartment> apartmentsPage = apartmentService.findAll(page, size, sort, dir);

        model.addAttribute("apartments", apartmentsPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", apartmentsPage.getTotalPages());
        model.addAttribute("size", size);
        model.addAttribute("sort", sort);
        model.addAttribute("dir", dir);

        return "admin/apartments/list";
    }


    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("apartment", new Apartment());
        return "admin/apartments/form";
    }

    @PostMapping
    public String save(@ModelAttribute Apartment apartment) {
        apartmentService.save(apartment);
        return "redirect:/admin/apartments";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        Apartment apartment = apartmentService.findById(id).orElseThrow();
        model.addAttribute("apartment", apartment);
        return "admin/apartments/form";
    }

    @PostMapping("/edit/{id}")
    public String update(@PathVariable Long id, @ModelAttribute Apartment apartment) {
        apartment.setId(id);
        apartmentService.save(apartment);
        return "redirect:/admin/apartments";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        apartmentService.deleteById(id);
        return "redirect:/admin/apartments";
    }
}
