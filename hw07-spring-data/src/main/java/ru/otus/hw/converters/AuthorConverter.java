package ru.otus.hw.converters;

import org.springframework.stereotype.Component;
import ru.otus.hw.models.Author;

// Тут специально ограничил видимость имплементации, чтобы можно было инжектить только дженерик !!!
@Component
class AuthorConverter implements ModelConverter<Author> {

    @Override
    public String convertToString(Author author) {
        return "Id: %d, FullName: %s".formatted(author.getId(), author.getFullName());
    }
}
