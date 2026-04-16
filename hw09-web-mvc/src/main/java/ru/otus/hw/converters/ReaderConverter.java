package ru.otus.hw.converters;

import org.springframework.stereotype.Component;
import ru.otus.hw.models.Reader;

@Component
class ReaderConverter implements ModelConverter<Reader> {

    @Override
    public String convertToString(Reader model) {
        return "Id: %d, FullName: %s".formatted(model.getId(), model.getFullName());
    }
}
