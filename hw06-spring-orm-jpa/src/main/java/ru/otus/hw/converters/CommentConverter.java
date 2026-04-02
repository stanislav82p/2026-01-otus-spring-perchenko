package ru.otus.hw.converters;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.hw.models.Comment;

import java.text.DateFormat;

@Component
@RequiredArgsConstructor
public class CommentConverter implements ModelConverter<Comment> {

    private final DateFormat dateFormatter;

    @Override
    public String convertToString(Comment comment) {

        return "Id: %d, Book: %s(%d), Reader: %s(%d), Text: %s, Date: %s".formatted(
                comment.getId(),
                comment.getBook().getTitle(),
                comment.getBook().getId(),
                comment.getReader().getFullName(),
                comment.getReader().getId(),
                comment.getText(),
                dateFormatter.format(comment.getDate()));
    }
}
