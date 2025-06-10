package ru.itis.fisd.semestrovka.controller.operator;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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
    public String list(Model model) {
        List<CallbackRequest> callbackRequests = callbackRequestService.findAllOrderByStatusAndDate();
        System.out.println(callbackRequests);
        model.addAttribute("callbackRequests", callbackRequests);
        return "operator/callback/list";
    }

    @PostMapping("/{id}/mark-done")
    public String markDone(@PathVariable Long id) {
        callbackRequestService.markAsDone(id);
        return "redirect:/operator/callback";
    }

}
