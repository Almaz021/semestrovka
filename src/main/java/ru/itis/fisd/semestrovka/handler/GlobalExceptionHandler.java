package ru.itis.fisd.semestrovka.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import ru.itis.fisd.semestrovka.exception.*;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler({
            ApartmentAlreadySoldException.class,
            ApartmentNotFoundException.class,
            CallbackRequestNotFoundException.class,
            PurchaseNotFoundException.class,
            UserNotFoundException.class,
            ViewingTimeConflictException.class,
            ViewingTimeOutOfBoundsException.class
    })
    public ModelAndView handleKnownExceptions(RuntimeException ex) {
        log.warn("Handled known exception: {}", ex.getMessage());

        ModelAndView mav = new ModelAndView("error/custom-error");
        mav.addObject("errorMessage", ex.getMessage());
        return mav;
    }

    @ExceptionHandler(DuplicateUserException.class)
    public ModelAndView handleDuplicateUserException(DuplicateUserException ex) {
        ModelAndView mav = new ModelAndView("/register");
        mav.addObject("error", ex.getMessage());
        return mav;
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ModelAndView handleAccessDenied() {
        return new ModelAndView("error/403");
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ModelAndView handleNoResourceFoundException(NoResourceFoundException ex) {
        return new ModelAndView("error/404");
    }

    @ExceptionHandler(Exception.class)
    public ModelAndView handleGenericException(Exception ex) {
        log.error("Unhandled exception occurred", ex);

        return new ModelAndView("error/500");
    }
}
