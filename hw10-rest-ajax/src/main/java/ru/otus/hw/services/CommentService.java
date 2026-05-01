package ru.otus.hw.services;

import ru.otus.hw.models.dto.CommentDto;
import ru.otus.hw.models.dto.CommentLightDto;
import ru.otus.hw.models.entity.BookEntity;
import ru.otus.hw.models.entity.ReaderEntity;
import ru.otus.hw.utils.EntityId;

import java.util.List;

public interface CommentService {

    CommentDto findById(Long commentId);

    List<CommentLightDto> findAll();

    List<CommentDto> findAllForBook(Long bookId);

    List<CommentDto> findAllFromReader(Long readerId);

    List<CommentDto> findAllForBookFromReader(Long bookId, Long readerId);

    void deleteById(Long commentId);

    int deleteAllFromReader(Long readerId);

    CommentDto createComment(EntityId<ReaderEntity> readerId, EntityId<BookEntity> bookId, String text);

    CommentLightDto updateComment(long commentId, String text);
}
