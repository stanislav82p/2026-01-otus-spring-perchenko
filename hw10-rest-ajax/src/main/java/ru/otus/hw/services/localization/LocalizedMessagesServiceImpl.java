package ru.otus.hw.services.localization;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

@Service
public class LocalizedMessagesServiceImpl implements LocalizedMessagesService {

    private final MessageSource messageSource;

    @Autowired
    public LocalizedMessagesServiceImpl(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @Override
    public String getMessage(String code, Object... args) {
        var locale = LocaleContextHolder.getLocale();
        return messageSource.getMessage(code, args, locale);
    }
}
