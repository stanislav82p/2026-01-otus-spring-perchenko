package ru.otus.hw.converters;

import org.springframework.stereotype.Component;
import ru.otus.hw.models.dto.ReaderDto;

@Component
class ReaderConverter implements ModelConverter<ReaderDto> {

    @Override
    public String convertToString(ReaderDto model) {
        return "Id: %d, FullName: %s".formatted(model.getId(), model.getFullName());
    }
}
