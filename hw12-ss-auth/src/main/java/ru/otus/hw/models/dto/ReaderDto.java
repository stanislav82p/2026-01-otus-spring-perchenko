package ru.otus.hw.models.dto;

import lombok.Getter;
import ru.otus.hw.models.entity.ReaderEntity;

@Getter
public class ReaderDto {
    private final String username;

    private final String firstName;

    private final String lastName;

    private final String fullName;

    public ReaderDto(String username, String firstName, String lastName, String fullName) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.fullName = fullName;
    }

    public static ReaderDto fromEntity(ReaderEntity reader) {
        return new ReaderDto(
                reader.getUsername(),
                reader.getFirstName(),
                reader.getLastName(),
                reader.getFullName()
        );
    }
}
