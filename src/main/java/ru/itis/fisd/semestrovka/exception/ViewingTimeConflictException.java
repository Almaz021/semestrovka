package ru.itis.fisd.semestrovka.exception;

import java.time.LocalDateTime;

public class ViewingTimeConflictException extends RuntimeException {
    public ViewingTimeConflictException(LocalDateTime time) {
        super("Have a viewing time conflict for %s".formatted(time));
    }
}
