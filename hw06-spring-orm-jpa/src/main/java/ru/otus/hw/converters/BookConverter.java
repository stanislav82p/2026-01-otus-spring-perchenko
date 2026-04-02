package ru.otus.hw.converters;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;

import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
// Тут специально ограничил видимость имплементации, чтобы можно было инжектить только дженерик !!!
class BookConverter implements ModelConverter<Book> {
    private final ModelConverter<Author> authorConverter;

    private final ModelConverter<Genre> genreConverter;

    @Override
    public String convertToString(Book book) {
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
