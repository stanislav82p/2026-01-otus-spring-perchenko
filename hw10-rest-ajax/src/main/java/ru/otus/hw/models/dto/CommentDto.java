package ru.otus.hw.models.dto;

import lombok.Getter;
import ru.otus.hw.models.entity.CommentEntity;

import java.sql.Date;

@Getter
public class CommentDto {
    private final long id;

    private final BookDto book;

    private final ReaderDto reader;

    private final String text;

    private final Date date;

    private CommentDto(long id, BookDto book, ReaderDto reader, String text, Date date) {
        this.id = id;
        this.book = book;
        this.reader = reader;
        this.text = text;
        this.date = date;
    }

    public static CommentDto fromEntity(CommentEntity comment) {
        return new CommentDto(
                comment.getId(),
                BookDto.fromEntity(comment.getBook()),
                ReaderDto.fromEntity(comment.getReader()),
                comment.getText(),
                comment.getDate()
        );
    }
}
