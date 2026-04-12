package ru.otus.hw.models.dto;

import lombok.Getter;
import ru.otus.hw.models.Reader;

@Getter
public class ReaderDto {
    private final long id;

    private final String fullName;

    private ReaderDto(long id, String fullName) {
        this.id = id;
        this.fullName = fullName;
    }

    public static ReaderDto fromEntity(Reader reader) {
        return new ReaderDto(reader.getId(), reader.getFullName());
    }
}
