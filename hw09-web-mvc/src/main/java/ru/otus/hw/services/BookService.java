package ru.otus.hw.services;

import ru.otus.hw.models.dto.BookDto;

import java.util.List;
import java.util.Set;

public interface BookService {
    BookDto findById(long id);

    List<BookDto> findAll();

    List<BookDto> findAllOfAuthor(long authorId);

    List<BookDto> findAllOfGenre(long genreId);

    List<BookDto> findAllOfAuthorAndGenre(long authorId, long genreId);

    BookDto insert(String title, long authorId, Set<Long> genresIds);

    BookDto update(long id, String title, long authorId, Set<Long> genresIds);

    void deleteById(long id);
}
