package ru.otus.hw.models.dto;

import lombok.Getter;
import ru.otus.hw.models.Genre;
import ru.otus.hw.models.entity.GenreEntity;

@Getter
public class GenreDto implements Genre {
    private final long id;

    private final String name;

    private GenreDto(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static GenreDto fromGenre(GenreEntity genre) {
        return new GenreDto(genre.getId(), genre.getName());
    }
}
