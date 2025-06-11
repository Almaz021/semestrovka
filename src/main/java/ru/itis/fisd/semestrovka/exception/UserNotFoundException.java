package ru.itis.fisd.semestrovka.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String username) {
        super("User with username: %s not found".formatted(username));
    }
}
