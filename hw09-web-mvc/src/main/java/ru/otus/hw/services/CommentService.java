package ru.otus.hw.services;

import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.models.Reader;
import ru.otus.hw.models.dto.CommentDto;
import ru.otus.hw.utils.EntityId;

import java.util.List;

public interface CommentService {

    CommentDto findById(Long commentId);

    List<CommentDto> findAll();

    List<CommentDto> findAllForBook(Long bookId);

    List<CommentDto> findAllForBookFromReader(Long bookId, Long readerId);

    void deleteById(Long commentId);

    int deleteAllFromReader(Long readerId);

    CommentDto createComment(EntityId<Reader> readerId, EntityId<Book> bookId, String text);

    CommentDto updateComment(long commentId, String text);
}
