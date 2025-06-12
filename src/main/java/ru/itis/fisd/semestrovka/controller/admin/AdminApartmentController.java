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
import ru.itis.fisd.semestrovka.dto.request.ApartmentFormRequest;
import ru.itis.fisd.semestrovka.dto.request.ApartmentListRequest;
import ru.itis.fisd.semestrovka.entity.dto.ApartmentDto;
import ru.itis.fisd.semestrovka.entity.orm.Apartment;
import ru.itis.fisd.semestrovka.mapper.ApartmentMapper;
import ru.itis.fisd.semestrovka.service.ApartmentService;

@Controller
@RequestMapping("/admin/apartments")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
@Slf4j
public class AdminApartmentController {

    private final ApartmentService apartmentService;
    private final ApartmentMapper apartmentMapper;

    @GetMapping
    public String list(@Valid ApartmentListRequest request, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("errors", result.getAllErrors());
            return "admin/apartments/list";
        }

        Page<ApartmentDto> apartmentsPage = apartmentService.findAll(
                request.getPage(),
                request.getSize(),
                request.getSort(),
                request.getDir());

        model.addAttribute("apartments", apartmentsPage.getContent());
        model.addAttribute("currentPage", request.getPage());
        model.addAttribute("totalPages", apartmentsPage.getTotalPages());
        model.addAttribute("size", request.getSize());
        model.addAttribute("sort", request.getSort());
        model.addAttribute("dir", request.getDir());

        return "admin/apartments/list";
    }


    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("apartment", new Apartment());
        log.debug("Show admin apartments form");
        return "admin/apartments/form";
    }

    @PostMapping
    public String save(@Valid @ModelAttribute("apartment") ApartmentFormRequest form,
                       BindingResult result,
                       Model model) {
        if (result.hasErrors()) {
            model.addAttribute("errors", result.getAllErrors());
            return "admin/apartments/form";
        }

        Apartment entity = apartmentMapper.toEntity(form);
        apartmentService.save(entity);
        return "redirect:/admin/apartments";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        log.debug("Prepare admin edit apartments form");
        ApartmentDto apartment = apartmentService.findDtoById(id);
        model.addAttribute("apartment", apartment);
        log.debug("Show admin edit apartments form");
        return "admin/apartments/form";
    }

    @PostMapping("/edit/{id}")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute("apartment") ApartmentFormRequest form,
                         BindingResult result,
                         Model model) {
        if (result.hasErrors()) {
            model.addAttribute("errors", result.getAllErrors());
            return "admin/apartments/form";
        }

        Apartment entity = apartmentMapper.toEntity(form);
        entity.setId(id);
        apartmentService.save(entity);
        return "redirect:/admin/apartments";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        log.debug("Handle admin delete apartments form");
        apartmentService.deleteById(id);
        return "redirect:/admin/apartments";
    }
}
