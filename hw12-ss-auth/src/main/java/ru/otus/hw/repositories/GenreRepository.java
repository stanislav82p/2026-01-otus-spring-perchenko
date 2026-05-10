package ru.otus.hw.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.hw.models.entity.GenreEntity;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface GenreRepository extends JpaRepository<GenreEntity, Long> {
    List<GenreEntity> findAll();

    Optional<GenreEntity> findById(long id);

    Set<GenreEntity> findByIdIn(Set<Long> ids);
}
