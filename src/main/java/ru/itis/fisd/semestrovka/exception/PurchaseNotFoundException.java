package ru.itis.fisd.semestrovka.exception;

public class PurchaseNotFoundException extends RuntimeException {
    public PurchaseNotFoundException(Long id) {
        super("Purchase with id = %s not fount".formatted(id));
    }
}
