package ru.otus.hw.services;

import ru.otus.hw.models.dto.CommentDto;
import ru.otus.hw.models.dto.CommentLightDto;

import java.util.List;

public interface CommentService {

    CommentDto findById(Long commentId);

    List<CommentLightDto> findAll();

    List<CommentDto> findAllForBook(Long bookId);

    List<CommentDto> findAllFromReader(String username);

    List<CommentDto> findAllForBookFromReader(Long bookId, String username);

    void deleteById(Long commentId);

    int deleteAllFromReader(String username);

    CommentDto createComment(String username, long bookId, String text);

    CommentLightDto updateComment(long commentId, String text);
}
