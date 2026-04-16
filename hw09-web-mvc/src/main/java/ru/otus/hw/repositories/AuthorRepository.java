package ru.otus.hw.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.hw.models.entity.AuthorEntity;

import java.util.Collection;
import java.util.List;


public interface AuthorRepository extends JpaRepository<AuthorEntity, Long> {

    List<AuthorEntity> findByIdIn(Collection<Long> ids);
}
