package ru.otus.hw.converters;

import org.springframework.stereotype.Component;
import ru.otus.hw.models.Genre;

@Component
// Тут специально ограничил видимость имплементации, чтобы можно было инжектить только дженерик !!!
class GenreConverter implements ModelConverter<Genre> {

    @Override
    public String convertToString(Genre genre) {
        return "Id: %d, Name: %s".formatted(genre.getId(), genre.getName());
    }
}
