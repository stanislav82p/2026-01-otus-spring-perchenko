package ru.otus.hw.converters;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.hw.models.dto.AuthorDto;
import ru.otus.hw.models.dto.BookDto;
import ru.otus.hw.models.dto.GenreDto;

import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
// Тут специально ограничил видимость имплементации, чтобы можно было инжектить только дженерик !!!
class BookConverter implements ModelConverter<BookDto> {
    private final ModelConverter<AuthorDto> authorConverter;

    private final ModelConverter<GenreDto> genreConverter;

    @Override
    public String convertToString(BookDto book) {
        var genresString = book.getGenres().stream()
                .map(genreConverter::convertToString)
                .map("{%s}"::formatted)
                .collect(Collectors.joining(", "));
        return "Id: %d, title: %s, author: {%s}, genres: [%s]".formatted(
                book.getId(),
                book.getTitle(),
                authorConverter.convertToString(book.getAuthor()),
                genresString);
    }
}
