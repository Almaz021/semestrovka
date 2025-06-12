package ru.itis.fisd.semestrovka.controller.operator;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.itis.fisd.semestrovka.dto.request.CallbackListRequest;
import ru.itis.fisd.semestrovka.entity.dto.CallbackRequestDto;
import ru.itis.fisd.semestrovka.service.CallbackRequestService;

@Controller
@RequestMapping("/operator/callback")
@RequiredArgsConstructor
@Slf4j
public class OperatorCallbackController {

    private final CallbackRequestService callbackRequestService;

    @GetMapping
    public String list(@Valid CallbackListRequest request,
                       BindingResult result,
                       Model model) {

        log.debug("Prepare operator callback list page");

        if (result.hasErrors()) {
            model.addAttribute("errors", result.getAllErrors());
            log.info("There are validation errors in callback list request");
            log.info("Errors: {}", result.getAllErrors());
            return "operator/callback/list";
        }

        Page<CallbackRequestDto> callbackRequests = callbackRequestService.findAllByStatusAndDate(
                request.getPage(), request.getSize());

        model.addAttribute("callbackRequests", callbackRequests);
        model.addAttribute("currentPage", request.getPage());
        model.addAttribute("size", request.getSize());

        log.debug("Show operator callback list page");
        return "operator/callback/list";
    }


    @PostMapping("/{id}/mark-done")
    @ResponseBody
    public ResponseEntity<String> markDone(@PathVariable Long id) {
        try {
            callbackRequestService.markAsDone(id);
            log.info("Mark operator callback done");
            return ResponseEntity.ok("marked as done");
        } catch (Exception e) {
            log.error("Error marking callback as done for id: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("error");
        }
    }
}
