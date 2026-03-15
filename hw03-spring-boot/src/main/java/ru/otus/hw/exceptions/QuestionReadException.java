package ru.otus.hw.exceptions;

import java.io.IOException;

public class QuestionReadException extends IOException {
    public QuestionReadException(String message, Throwable ex) {
        super(message, ex);
    }

    public QuestionReadException(String message) {
        super(message);
    }
}
