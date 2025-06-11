package ru.itis.fisd.semestrovka.exception;

public class ApartmentNotFoundException extends RuntimeException {
    public ApartmentNotFoundException(Long id) {
        super("Apartment with id = %s not found".formatted(id));
    }
}
