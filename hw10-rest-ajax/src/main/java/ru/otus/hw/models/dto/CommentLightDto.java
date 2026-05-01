package ru.otus.hw.models.dto;

import lombok.Getter;
import ru.otus.hw.models.entity.CommentEntity;

import java.sql.Date;

@Getter
public class CommentLightDto {
    private final long id;

    private final long bookId;

    private final ReaderDto reader;

    private final String text;

    private final Date date;

    private CommentLightDto(long id, long bookId, ReaderDto reader, String text, Date date) {
        this.id = id;
        this.bookId = bookId;
        this.reader = reader;
        this.text = text;
        this.date = date;
    }

    public static CommentLightDto fromEntity(CommentEntity comment) {
        return new CommentLightDto(
                comment.getId(),
                comment.getBook().getId(),
                ReaderDto.fromEntity(comment.getReader()),
                comment.getText(),
                comment.getDate()
        );
    }
}
