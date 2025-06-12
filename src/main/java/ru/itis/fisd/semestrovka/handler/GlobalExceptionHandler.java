package ru.itis.fisd.semestrovka.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import ru.itis.fisd.semestrovka.exception.*;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler({
            ApartmentAlreadySoldException.class,
            ApartmentNotFoundException.class,
            CallbackRequestNotFoundException.class,
            DuplicateUserException.class,
            PurchaseNotFoundException.class,
            UserDoubleBookingException.class,
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

    @ExceptionHandler(Exception.class)
    public ModelAndView handleGenericException(Exception ex) {
        log.error("Unhandled exception occurred", ex);

        return new ModelAndView("error/500");
    }
}
