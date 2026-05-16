package ru.otus.hw.converters;

import org.springframework.stereotype.Component;
import ru.otus.hw.models.dto.GenreDto;

@Component
// Тут специально ограничил видимость имплементации, чтобы можно было инжектить только дженерик !!!
class GenreConverter implements ModelConverter<GenreDto> {

    @Override
    public String convertToString(GenreDto genre) {
        return "Id: %d, Name: %s".formatted(genre.getId(), genre.getName());
    }
}
