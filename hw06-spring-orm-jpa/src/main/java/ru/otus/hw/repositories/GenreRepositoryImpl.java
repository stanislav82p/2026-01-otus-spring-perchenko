package ru.otus.hw.repositories;

import org.springframework.stereotype.Repository;
import ru.otus.hw.models.Genre;

import java.util.List;
import java.util.Set;

@Repository
public class GenreRepositoryImpl implements GenreRepository{
    @Override
    public List<Genre> findAll() {
        return List.of();
    }

    @Override
    public Set<Genre> findAllByIds(Set<Long> ids) {
        return Set.of();
    }
}
