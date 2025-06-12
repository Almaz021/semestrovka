package ru.itis.fisd.semestrovka.exception;

public class ViewingTimeOutOfBoundsException extends RuntimeException {
    public ViewingTimeOutOfBoundsException() {
        super("Can view only from 8:00 to 18:00");
    }
}
