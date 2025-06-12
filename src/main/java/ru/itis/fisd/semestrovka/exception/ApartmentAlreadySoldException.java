package ru.itis.fisd.semestrovka.exception;

public class ApartmentAlreadySoldException extends RuntimeException {
    public ApartmentAlreadySoldException(Long apartmentId) {
        super("Apartment with id = %s already sold".formatted(apartmentId));
    }
}
