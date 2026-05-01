package ru.otus.hw.exceptions;

public class BadHttpRequestParamsException extends RuntimeException {

    private final String userMessage;

    public BadHttpRequestParamsException(String message, String userMessage) {
        super(message);
        this.userMessage = userMessage;
    }

    public String getUserMessage() {
        return userMessage;
    }
}
