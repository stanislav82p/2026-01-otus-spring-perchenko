package ru.otus.hw.models;

import java.util.Set;

public interface Book {
    long getId();

    String getTitle();

    Author getAuthor();

    Set<? extends Genre> getGenres();
}
