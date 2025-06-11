package ru.itis.fisd.semestrovka.exception;

public class DuplicateUserException extends RuntimeException {
    public DuplicateUserException(String username) {
        super("User with username: %s already exists".formatted(username));
    }
}
