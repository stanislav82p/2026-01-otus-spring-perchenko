package ru.otus.hw.services;

import ru.otus.hw.models.Genre;
import ru.otus.hw.models.dto.GenreDto;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface GenreService {
    List<GenreDto> findAll();

    GenreDto findById(long id);

    Map<Long, GenreDto> findByIds(Set<Long> ids);
}
