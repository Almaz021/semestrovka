package ru.itis.fisd.semestrovka.controller.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.itis.fisd.semestrovka.entity.orm.Apartment;
import ru.itis.fisd.semestrovka.service.ApartmentService;

@Controller
@RequestMapping("/admin/apartments")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
@Slf4j
public class AdminApartmentController {

    private final ApartmentService apartmentService;

    @GetMapping
    public String list(Model model,
                       @RequestParam(value = "page", required = false, defaultValue = "0") int page,
                       @RequestParam(value = "size", required = false, defaultValue = "10") int size,
                       @RequestParam(value = "sort", required = false, defaultValue = "id") String sort,
                       @RequestParam(value = "dir", required = false, defaultValue = "asc") String dir) {

        log.debug("Prepare admin apartments list page");

        Page<Apartment> apartmentsPage = apartmentService.findAll(page, size, sort, dir);

        model.addAttribute("apartments", apartmentsPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", apartmentsPage.getTotalPages());
        model.addAttribute("size", size);
        model.addAttribute("sort", sort);
        model.addAttribute("dir", dir);

        log.debug("Show admin apartments list page");

        return "admin/apartments/list";
    }


    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("apartment", new Apartment());
        log.debug("Show admin apartments form");
        return "admin/apartments/form";
    }

    @PostMapping
    public String save(@ModelAttribute Apartment apartment) {
        apartmentService.save(apartment);
        log.debug("Handle save apartment request");
        return "redirect:/admin/apartments";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        log.debug("Prepare admin edit apartments form");
        Apartment apartment = apartmentService.findById(id);
        model.addAttribute("apartment", apartment);
        log.debug("Show admin edit apartments form");
        return "admin/apartments/form";
    }

    @PostMapping("/edit/{id}")
    public String update(@PathVariable Long id, @ModelAttribute Apartment apartment) {
        log.debug("Handle admin edit apartments form");
        apartment.setId(id);
        apartmentService.save(apartment);
        return "redirect:/admin/apartments";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        log.debug("Handle admin delete apartments form");
        apartmentService.deleteById(id);
        return "redirect:/admin/apartments";
    }
}
