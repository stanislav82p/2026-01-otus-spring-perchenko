package ru.otus.hw.repositories;

import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.models.Reader;

import java.util.List;
import java.util.Optional;

public interface CommentRepository {

    List<Comment> findAll();

    Optional<Comment> findById(long id);

    List<Comment> findAllForBook(long bookId);

    List<Comment> findAllFromReader(long readerId);

    List<Comment> findAllFromReaderForBook(Reader reader, Book book);

    Comment createComment(Book book, Reader reader, String text);

    int deleteById(long commentId);

    int deleteAllFromReader(long readerId);

    boolean update(long commentId, String text);
}
