package ru.otus.hw.models.dto;

import lombok.Getter;
import ru.otus.hw.models.entity.ReaderEntity;

@Getter
public class ReaderDto {
    private final long id;

    private final String fullName;

    private ReaderDto(long id, String fullName) {
        this.id = id;
        this.fullName = fullName;
    }

    public static ReaderDto fromEntity(ReaderEntity reader) {
        return new ReaderDto(reader.getId(), reader.getFullName());
    }
}
