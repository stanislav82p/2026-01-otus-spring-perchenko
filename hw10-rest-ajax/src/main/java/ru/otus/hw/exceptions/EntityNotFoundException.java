package ru.otus.hw.exceptions;

public class EntityNotFoundException extends RuntimeException {

    private final String userMessage;

    public EntityNotFoundException(String message, String userMessage) {
        super(message);
        this.userMessage = userMessage;
    }

    public String getUserMessage() {
        return userMessage;
    }
}
