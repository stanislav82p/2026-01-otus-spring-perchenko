package ru.otus.hw.service.localization;

public interface LocalizedMessagesService {
    String getMessage(String code, Object ...args);
}
