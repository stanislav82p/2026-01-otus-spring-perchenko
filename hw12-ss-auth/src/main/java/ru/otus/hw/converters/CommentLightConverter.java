package ru.otus.hw.converters;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.hw.models.dto.CommentLightDto;
import ru.otus.hw.models.dto.ReaderDto;

import java.text.DateFormat;

@Component
@RequiredArgsConstructor
public class CommentLightConverter implements ModelConverter<CommentLightDto> {

    private final DateFormat dateFormatter;

    private final ModelConverter<ReaderDto> readerConverter;

    @Override
    public String convertToString(CommentLightDto comment) {

        return "Id: %d, Book ID: %d, Reader: {%s}, Text: %s, Date: %s".formatted(
                comment.getId(),
                comment.getBookId(),
                readerConverter.convertToString(comment.getReader()),
                comment.getText(),
                dateFormatter.format(comment.getDate()));
    }
}
