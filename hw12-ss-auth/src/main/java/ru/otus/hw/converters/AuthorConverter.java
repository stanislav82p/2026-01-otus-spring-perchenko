package ru.otus.hw.converters;

import org.springframework.stereotype.Component;
import ru.otus.hw.models.dto.AuthorDto;

// Тут специально ограничил видимость имплементации, чтобы можно было инжектить только дженерик !!!
@Component
class AuthorConverter implements ModelConverter<AuthorDto> {

    @Override
    public String convertToString(AuthorDto author) {
        return "Id: %d, FullName: %s".formatted(author.getId(), author.getFullName());
    }
}
