package ru.otus.hw.converters;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.models.Reader;
import ru.otus.hw.models.dto.BookDto;
import ru.otus.hw.models.dto.CommentDto;
import ru.otus.hw.models.dto.ReaderDto;

import java.text.DateFormat;

@Component
@RequiredArgsConstructor
public class CommentConverter implements ModelConverter<CommentDto> {

    private final DateFormat dateFormatter;

    private final ModelConverter<ReaderDto> readerConverter;

    private final ModelConverter<BookDto> bookConverter;

    @Override
    public String convertToString(CommentDto comment) {

        return "Id: %d, Book: {%s}, Reader: {%s}, Text: %s, Date: %s".formatted(
                comment.getId(),
                bookConverter.convertToString(comment.getBook()),
                readerConverter.convertToString(comment.getReader()),
                comment.getText(),
                dateFormatter.format(comment.getDate()));
    }
}
