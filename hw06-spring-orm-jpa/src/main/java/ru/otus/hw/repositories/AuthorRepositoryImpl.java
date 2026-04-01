package ru.otus.hw.repositories;

import org.springframework.stereotype.Repository;
import ru.otus.hw.models.Author;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Repository
public class AuthorRepositoryImpl implements AuthorRepository{
    @Override
    public List<Author> findAll() {
        return List.of();
    }

    @Override
    public Optional<Author> findById(long id) {
        return Optional.empty();
    }

    @Override
    public Map<Long, Author> findByIds(Set<Long> ids) {
        return Map.of();
    }
}
