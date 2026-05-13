package ru.otus.hw.rest;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.hw.exceptions.BadHttpRequestParamsException;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.dto.CommentDto;
import ru.otus.hw.models.dto.CommentLightDto;
import ru.otus.hw.rest.request.CommentCreationRequestDto;
import ru.otus.hw.rest.request.CommentUpdateRequestDto;
import ru.otus.hw.rest.response.CommentsDeleteResponseDto;
import ru.otus.hw.rest.response.EntityUpdateResponseDto;
import ru.otus.hw.services.CommentService;
import ru.otus.hw.services.localization.LocalizedMessagesService;

import java.util.List;
import java.util.Optional;

import static ru.otus.hw.rest.utils.BadEntityResponseProvider.getBadEntityResponse;

@RestController
public class CommentApiConstroller {

    private final CommentService commentService;


    private final LocalizedMessagesService messageService;

    @Autowired
    public CommentApiConstroller(CommentService commentService, LocalizedMessagesService messageService) {
        this.commentService = commentService;
        this.messageService = messageService;
    }

    @GetMapping(path = "/api/library/comments/{id}")
    public CommentDto getCommentById(@PathVariable("id") long commentId) {
        return commentService.findById(commentId);
    }

    @GetMapping(path = "api/library/comments", params = {"book_id"})
    public ResponseEntity<List<CommentDto>> getAllCommentsForBook(
            @RequestParam(value = "book_id", defaultValue = "0") long bookId
    ) {
        if (bookId > 0) {
            var comments = commentService.findAllForBook(bookId);
            return ResponseEntity.ok().body(comments);
        } else {
            throw new BadHttpRequestParamsException(
                    "Book ID %d is not acceptable".formatted(bookId),
                    messageService.getMessage("user_message_wrong_request")
            );
        }
    }

    @GetMapping(path = "api/library/comments", params = {"book_id", "reader_id"})
    public ResponseEntity<List<CommentDto>> getAllCommentsForBookFromReader(
            @RequestParam(value = "book_id", defaultValue = "0") long bookId,
            @RequestParam(value = "reader_id", defaultValue = "") String readerId
    ) {
        if (!readerId.isEmpty() && (bookId > 0)) {
            var comments = commentService.findAllForBookFromReader(bookId, readerId);
            return ResponseEntity.ok().body(comments);
        } else {
            throw new BadHttpRequestParamsException(
                    "Book ID %d and/or Reader ID %s is not acceptable".formatted(bookId, readerId),
                    messageService.getMessage("user_message_wrong_request")
            );
        }
    }

    @DeleteMapping(path = "api/library/comments/{id}")
    public HttpEntity deleteComment(@PathVariable("id") long commentId) {
        commentService.deleteById(commentId);
        return HttpEntity.EMPTY;
    }

    @DeleteMapping(path = "api/library/comments", params = {"reader_id"})
    public CommentsDeleteResponseDto deleteAllCommentsFromReader(
            @RequestParam(value = "reader_id", defaultValue = "") String readerId
    ) {
        if (!readerId.isEmpty()) {
            int nDel = commentService.deleteAllFromReader(readerId);
            return new CommentsDeleteResponseDto(nDel);
        } else {
            throw new BadHttpRequestParamsException(
                    "Reader ID %s is not acceptable".formatted(readerId),
                    messageService.getMessage("user_message_wrong_request")
            );
        }
    }

    @PostMapping(path = "/api/library/comments")
    public ResponseEntity<EntityUpdateResponseDto<CommentDto>> createComment(
            @Valid @RequestBody CommentCreationRequestDto dto,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return getBadEntityResponse(bindingResult);
        }

        var comment = commentService.createComment(dto.getReaderId(), dto.getBookId(), dto.getText());
        var body = new EntityUpdateResponseDto<>(comment, null);
        return ResponseEntity.ok().body(body);
    }

    @PutMapping(path = "/api/library/comments/{id}")
    public ResponseEntity<EntityUpdateResponseDto<CommentLightDto>> updateComment(
            @PathVariable("id") long commentId,
            @Valid @RequestBody CommentUpdateRequestDto dto,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return getBadEntityResponse(bindingResult);
        }

        Optional<CommentDto> origComment = commentService.findAllFromReader(dto.getReaderId())
                .stream()
                .filter(it -> it.getId() == commentId)
                .findAny();
        if (origComment.isEmpty()) {
            throw new EntityNotFoundException(
                    "У читателя с ID %s нет комментария с ID %d".formatted(dto.getReaderId(), commentId),
                    messageService.getMessage("comment-not-found")
            );
        }

        CommentLightDto updComment = commentService.updateComment(commentId, dto.getText());
        var body = new EntityUpdateResponseDto<>(updComment, null);
        return ResponseEntity.ok().body(body);
    }


}
