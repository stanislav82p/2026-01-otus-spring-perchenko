package ru.otus.hw.services;

import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.models.Reader;
import ru.otus.hw.utils.EntityId;

import java.util.List;

public interface CommentService {

    Comment findById(Long commentId);

    List<? extends Comment> findAll();

    List<? extends Comment> findAllForBook(Long bookId);

    List<? extends Comment> findAllForBookFromReader(Long bookId, Long readerId);

    void deleteById(Long commentId);

    int deleteAllFromReader(Long readerId);

    Comment createComment(EntityId<Reader> readerId, EntityId<Book> bookId, String text);

    Comment updateComment(long commentId, String text);
}
