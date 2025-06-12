package ru.itis.fisd.semestrovka.exception;

public class CallbackRequestNotFoundException extends RuntimeException {
    public CallbackRequestNotFoundException(Long id) {
        super("Callback request with id = %s not found".formatted(id));
    }
}
