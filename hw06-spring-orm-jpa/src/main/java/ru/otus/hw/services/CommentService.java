package ru.otus.hw.services;

import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.models.Reader;
import ru.otus.hw.utils.EntityId;

import java.util.List;
import java.util.Optional;

public interface CommentService {

    Optional<Comment> findById(EntityId<Comment> commentId);

    List<Comment> findAll();

    List<Comment> findAllForBook(EntityId<Book> bookId);

    List<Comment> findAllForBookFromReader(EntityId<Book> bookId, EntityId<Reader> readerId);

    boolean deleteById(EntityId<Comment> commentId);

    int deleteAllFromReader(EntityId<Reader> readerId);

    Comment createComment(EntityId<Reader> readerId, EntityId<Book> bookId, String text);

    Comment updateComment(EntityId<Comment> commentId, String text);
}
