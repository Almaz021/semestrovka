package ru.itis.fisd.semestrovka.exception;

import java.time.LocalDateTime;

public class UserDoubleBookingException extends RuntimeException {
    public UserDoubleBookingException(Long id, LocalDateTime bookingDate) {
        super("User with id %s already has booking on this time %s".formatted(id, bookingDate));
    }
}
