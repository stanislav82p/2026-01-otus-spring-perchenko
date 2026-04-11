package ru.otus.hw.converters;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.hw.models.Comment;
import ru.otus.hw.models.Reader;

import java.text.DateFormat;

@Component
@RequiredArgsConstructor
public class CommentConverter implements ModelConverter<Comment> {

    private final DateFormat dateFormatter;

    private final ModelConverter<Reader> readerConverter;

    @Override
    public String convertToString(Comment comment) {

        return "Id: %d, Book ID: %d, Reader: {%s}, Text: %s, Date: %s".formatted(
                comment.getId(),
                comment.getBook().getId(),
                readerConverter.convertToString(comment.getReader()),
                comment.getText(),
                dateFormatter.format(comment.getDate()));
    }
}
