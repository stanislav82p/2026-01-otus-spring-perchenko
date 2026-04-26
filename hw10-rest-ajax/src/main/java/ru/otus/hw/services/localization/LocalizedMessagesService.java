package ru.otus.hw.services.localization;

public interface LocalizedMessagesService {
    String getMessage(String code, Object ...args);
}