package ru.otus.hw.models.dto;

import lombok.Getter;
import ru.otus.hw.models.Author;

@Getter
public class AuthorDto {
    private final long id;

    private final String fullName;

    private AuthorDto(long id, String fullName) {
        this.id = id;
        this.fullName = fullName;
    }

    public static AuthorDto fromEntity(Author author) {
        return new AuthorDto(author.getId(), author.getFullName());
    }
}
