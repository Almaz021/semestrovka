package ru.itis.fisd.semestrovka.controller.operator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.itis.fisd.semestrovka.entity.orm.CallbackRequest;
import ru.itis.fisd.semestrovka.service.CallbackRequestService;

@Controller
@RequestMapping("/operator/callback")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN', 'OPERATOR')")
@Slf4j
public class OperatorCallbackController {

    private final CallbackRequestService callbackRequestService;

    @GetMapping
    public String list(
            Model model,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        log.debug("Prepare operator callback list page");
        Page<CallbackRequest> callbackRequests = callbackRequestService.findAllByStatusAndDate(page, size);
        model.addAttribute("callbackRequests", callbackRequests);

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
