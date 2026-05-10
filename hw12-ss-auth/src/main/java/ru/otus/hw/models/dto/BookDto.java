package ru.otus.hw.models.dto;

import lombok.Getter;
import ru.otus.hw.models.entity.BookEntity;

import java.util.Set;
import java.util.stream.Collectors;

@Getter
public class BookDto {
    private final long id;

    private final String title;

    private final AuthorDto author;

    private final Set<GenreDto> genres;

    private BookDto(long id, String title, AuthorDto author, Set<GenreDto> genres) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.genres = genres;
    }

    public static BookDto fromEntity(BookEntity book) {
        return new BookDto(
                book.getId(),
                book.getTitle(),
                AuthorDto.fromEntity(book.getAuthor()),
                book.getGenres().stream()
                        .map(GenreDto::fromGenre)
                        .collect(Collectors.toSet())
        );
    }
}
