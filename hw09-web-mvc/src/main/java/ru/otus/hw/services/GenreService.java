package ru.otus.hw.services;

import ru.otus.hw.models.Genre;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface GenreService {
    List<Genre> findAll();

    Genre findById(long id);

    Map<Long, Genre> findByIds(Set<Long> ids);
}
