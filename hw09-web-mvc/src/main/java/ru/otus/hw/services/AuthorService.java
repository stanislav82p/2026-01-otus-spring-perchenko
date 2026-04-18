package ru.otus.hw.services;

import ru.otus.hw.models.Author;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface AuthorService {
    List<Author> findAll();

    Author findById(long id);

    Map<Long, Author> findByIds(Set<Long> ids);
}
