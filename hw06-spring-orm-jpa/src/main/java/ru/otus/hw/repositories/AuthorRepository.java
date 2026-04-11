package ru.otus.hw.repositories;

import ru.otus.hw.models.Author;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public interface AuthorRepository {
    List<Author> findAll();

    Optional<Author> findById(long id);

    Map<Long, Author> findByIds(Set<Long> ids);
}
