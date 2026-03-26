package ru.otus.hw.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.With;

import java.util.Set;

@AllArgsConstructor
@Getter
@With
@Builder
public class Book {
    private long id;

    private String title;

    private Author author;

    private Set<Genre> genres;
}
