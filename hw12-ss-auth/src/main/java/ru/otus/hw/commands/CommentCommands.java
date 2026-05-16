package ru.otus.hw.commands;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.otus.hw.converters.ModelConverter;
import ru.otus.hw.models.dto.CommentDto;
import ru.otus.hw.models.dto.CommentLightDto;
import ru.otus.hw.services.CommentService;

import java.util.stream.Collectors;

@RequiredArgsConstructor
@ShellComponent
public class CommentCommands {

    private final CommentService commentService;

    private final ModelConverter<CommentDto> commentConverter;

    private final ModelConverter<CommentLightDto> commentLightConverter;

    @ShellMethod(value = "Find all comments", key = "allcomments")
    public String findAllComments() {
        return commentService.findAll().stream()
                .map(commentLightConverter::convertToString)
                .collect(Collectors.joining("," + System.lineSeparator()));
    }

    @ShellMethod(value = "Find single comment by ID", key = "commentid")
    public String findById(@ShellOption(value = { "--id" }, help = "ID комментария") long commentId) {
        return commentConverter.convertToString(commentService.findById(commentId));
    }

    @ShellMethod(value = "Find all comments for a book", key = "bcomments")
    public String findCommentsForBook(
            @ShellOption(value = { "--book-id", "-b" }, help = "ID книги")
            long bookId
    ) {
        return commentService.findAllForBook(bookId).stream()
                .map(commentConverter::convertToString)
                .collect(Collectors.joining("," + System.lineSeparator()));
    }

    @ShellMethod(value = "Find all comments for a book from reader", key = "brcomments")
    public String findCommentsForBookFromReader(
            @ShellOption(value = { "--book-id", "-b" }, help = "ID книги")
            long bookId,

            @ShellOption(value = { "--reader-id", "-r" }, help = "ID читателя")
            String readerId
    ) {

        return commentService.findAllForBookFromReader(bookId, readerId).stream()
                .map(commentConverter::convertToString)
                .collect(Collectors.joining("," + System.lineSeparator()));
    }

    @ShellMethod(value = "Delete comment", key = "delcomment")
    public void deleteComment(
            @ShellOption(value = { "--comment-id", "-c" }, help = "ID комментария")
            long commentId
    ) {
        commentService.deleteById(commentId);
    }

    @ShellMethod(value = "Delete all comments from reader", key = "delcommreader")
    public void deleteCommentsFromReader(
            @ShellOption(value = { "--reader-id", "-r" }, help = "ID комментария")
            String readerId
    ) {
        commentService.deleteAllFromReader(readerId);
    }

    @ShellMethod(value = "Create comment", key = "makecomment")
    public String createComment(
            @ShellOption(value = { "--book-id", "-b" }, help = "ID книги")
            long bookId,

            @ShellOption(value = { "--reader-id", "-r" }, help = "ID читателя")
            String readerId,

            @ShellOption(value = { "--text", "-t" }, help = "текст комментария")
            String txt
    ) {
        CommentDto comment = commentService.createComment(readerId, bookId, txt);
        return "Comment was created: %s".formatted(commentConverter.convertToString(comment));
    }

    @ShellMethod(value = "Edit comment", key = "edcomment")
    public String updateComment(
            @ShellOption(value = { "--comment-id", "-c" }, help = "ID комментария")
            long commentId,

            @ShellOption(value = { "--text", "-t" }, help = "текст комментария")
            String txt
    ) {
        CommentLightDto comment = commentService.updateComment(commentId, txt);
        return "Comment was updated: %s".formatted(commentLightConverter.convertToString(comment));
    }
}
