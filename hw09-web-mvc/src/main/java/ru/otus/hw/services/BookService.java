package ru.otus.hw.services;

import ru.otus.hw.models.Book;

import java.util.List;
import java.util.Set;

public interface BookService {
    Book findById(long id);

    List<Book> findAll();

    List<Book> findAllOfAuthor(long authorId);

    List<Book> findAllOfGenre(long genreId);

    List<Book> findAllOfAuthorAndGenre(long authorId, long genreId);

    Book insert(String title, long authorId, Set<Long> genresIds);

    Book update(long id, String title, long authorId, Set<Long> genresIds);

    void deleteById(long id);
}
