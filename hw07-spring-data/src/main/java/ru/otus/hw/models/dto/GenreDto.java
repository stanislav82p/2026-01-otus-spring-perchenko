package ru.otus.hw.models.dto;

import lombok.Getter;
import ru.otus.hw.models.Genre;

@Getter
public class GenreDto {
    private final long id;

    private final String name;

    private GenreDto(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static GenreDto fromGenre(Genre genre) {
        return new GenreDto(genre.getId(), genre.getName());
    }
}
