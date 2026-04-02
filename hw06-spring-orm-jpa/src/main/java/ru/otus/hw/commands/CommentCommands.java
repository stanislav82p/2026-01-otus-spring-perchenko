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

import java.util.stream.Collectors;

@RequiredArgsConstructor
@ShellComponent
public class CommentCommands {

    private final CommentService commentService;

    private final ModelConverter<Comment> commentConverter;

    @ShellMethod(value = "Find all comments for a book", key = "bcomments")
    public String findCommentsForBook(
            @ShellOption(value = { "--book-id", "-b" }, help = "ID книги")
            long bookId
    ) {
        return commentService.findAllForBook(EntityId.forValue(bookId)).stream()
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
        EntityId<Book> bId   = EntityId.forValue(bookId);
        EntityId<Reader> rId = EntityId.forValue(readerId);

        return commentService.findAllForBookFromReader(bId, rId).stream()
                .map(commentConverter::convertToString)
                .collect(Collectors.joining("," + System.lineSeparator()));
    }

    @ShellMethod(value = "Delete comment", key = "delcomment")
    public String deleteComment(
            @ShellOption(value = { "--comment-id", "-c" }, help = "ID комментария")
            long commentId
    ) {
        boolean isOk = commentService.deleteById(EntityId.forValue(commentId));
        return isOk
                ? "Comment with ID %d was deleted".formatted(commentId)
                : "Comment with ID %d was NOT deleted".formatted(commentId);
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
