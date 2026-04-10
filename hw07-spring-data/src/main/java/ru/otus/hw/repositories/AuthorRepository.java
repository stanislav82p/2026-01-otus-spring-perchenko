package ru.otus.hw.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.hw.models.Author;

import java.util.Collection;
import java.util.List;


public interface AuthorRepository extends JpaRepository<Author, Long> {

    List<Author> findByIdIn(Collection<Long> ids);
}
