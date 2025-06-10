package ru.itis.fisd.semestrovka.controller.operator;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.itis.fisd.semestrovka.entity.CallbackRequest;
import ru.itis.fisd.semestrovka.service.CallbackRequestService;

import java.util.List;

@Controller
@RequestMapping("/operator/callback")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN', 'OPERATOR')")
public class OperatorCallbackController {

    private final CallbackRequestService callbackRequestService;

    @GetMapping
    public String list(
            Model model,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<CallbackRequest> callbackRequests = callbackRequestService.findAllOrderByStatusAndDate(pageable);
        model.addAttribute("callbackRequests", callbackRequests);
        return "operator/callback/list";
    }

//  ajax
    @PostMapping("/{id}/mark-done")
    public String markDone(@PathVariable Long id) {
        callbackRequestService.markAsDone(id);
        return "redirect:/operator/callback";
    }

}
