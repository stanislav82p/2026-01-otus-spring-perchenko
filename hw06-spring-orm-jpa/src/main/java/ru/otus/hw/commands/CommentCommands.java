package ru.otus.hw.commands;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.otus.hw.converters.ModelConverter;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.models.Reader;
import ru.otus.hw.services.CommentService;
import ru.otus.hw.utils.EntityId;

import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@ShellComponent
public class CommentCommands {

    private final CommentService commentService;

    private final ModelConverter<Comment> commentConverter;

    @ShellMethod(value = "Find all comments", key = "allcomments")
    public String findAllComments() {
        return commentService.findAll().stream()
                .map(commentConverter::convertToString)
                .collect(Collectors.joining("," + System.lineSeparator()));
    }

    @ShellMethod(value = "Find single comment by ID", key = "commentid")
    public String findById(@ShellOption(value = { "--id" }, help = "ID комментария") long commentId) {
        Optional<Comment> optComment = commentService.findById(EntityId.forValue(commentId));
        if (optComment.isPresent()) {
            return commentConverter.convertToString(optComment.get());
        } else {
            return "Комментарий с ID %d не найден".formatted(commentId);
        }
    }

    @ShellMethod(value = "Find all comments for a book", key = "bcomments")
    public String findCommentsForBook(
            @ShellOption(value = { "--book-id", "-b" }, help = "ID книги")
            long bookId
    ) {
        return commentService.findAllForBook(EntityId.forValue(bookId)).stream()
                .map(commentConverter::convertToString)
                .collect(Collectors.joining("," + System.lineSeparator()));
    }

    @ShellMethod(value = "Find all comments from reader", key = "rcomments")
    public String findCommentsFromReader(
            @ShellOption(value = { "--reader-id", "-r" }, help = "ID читателя")
            long readerId
    ) {
        return commentService.findAllFromReader(EntityId.forValue(readerId)).stream()
                .map(commentConverter::convertToString)
                .collect(Collectors.joining("," + System.lineSeparator()));
    }

    @ShellMethod(value = "Find all comments for a book from reader", key = "brcomments")
    public String findCommentsForBookFromReader(
            @ShellOption(value = { "--book-id", "-b" }, help = "ID книги")
            long bookId,

            @ShellOption(value = { "--reader-id", "-r" }, help = "ID читателя")
            long readerId
    ) {
        EntityId<Book> bId = EntityId.forValue(bookId);
        EntityId<Reader> rId = EntityId.forValue(readerId);
        return commentService.findAllForBookFromReader(bId, rId).stream()
                .map(commentConverter::convertToString)
                .collect(Collectors.joining("," + System.lineSeparator()));
    }

    @ShellMethod(value = "Delete comment", key = "delcomment")
    public void deleteComment(
            @ShellOption(value = { "--comment-id", "-c" }, help = "ID комментария")
            long commentId
    ) {
        commentService.deleteById(EntityId.forValue(commentId));
    }

    @ShellMethod(value = "Delete all comments from reader", key = "delcommreader")
    public void deleteCommentsFromReader(
            @ShellOption(value = { "--reader-id", "-r" }, help = "ID комментария")
            long readerId
    ) {
        commentService.deleteAllFromReader(EntityId.forValue(readerId));
    }

    @ShellMethod(value = "Create comment", key = "makecomment")
    public String createComment(
            @ShellOption(value = { "--book-id", "-b" }, help = "ID книги")
            long bookId,

            @ShellOption(value = { "--reader-id", "-r" }, help = "ID читателя")
            long readerId,

            @ShellOption(value = { "--text", "-t" }, help = "текст комментария")
            String txt
    ) {
        EntityId<Book> bId   = EntityId.forValue(bookId);
        EntityId<Reader> rId = EntityId.forValue(readerId);

        Comment comment = commentService.createComment(rId, bId, txt);
        return "Comment was created: %s".formatted(commentConverter.convertToString(comment));
    }

    @ShellMethod(value = "Edit comment", key = "edcomment")
    public String updateComment(
            @ShellOption(value = { "--comment-id", "-c" }, help = "ID комментария")
            long commentId,

            @ShellOption(value = { "--text", "-t" }, help = "текст комментария")
            String txt
    ) {
        Comment comment = commentService.updateComment(EntityId.forValue(commentId), txt);
        return "Comment was updated: %s".formatted(commentConverter.convertToString(comment));
    }
}
