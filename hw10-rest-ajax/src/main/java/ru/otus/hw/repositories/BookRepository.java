package ru.otus.hw.repositories;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.hw.models.entity.AuthorEntity;
import ru.otus.hw.models.entity.BookEntity;
import ru.otus.hw.models.entity.GenreEntity;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<BookEntity, Long> {

    @EntityGraph(attributePaths = {"author", "genres"}, type = EntityGraph.EntityGraphType.LOAD)
    @Override
    Optional<BookEntity> findById(Long id);

    @EntityGraph(attributePaths = {"author"}, type = EntityGraph.EntityGraphType.LOAD)
    @Override
    List<BookEntity> findAll();

    @EntityGraph(attributePaths = {"author"}, type = EntityGraph.EntityGraphType.LOAD)
    List<BookEntity> findByAuthor(AuthorEntity author);

    @EntityGraph(attributePaths = {"author"}, type = EntityGraph.EntityGraphType.LOAD)
    List<BookEntity> findByGenres(GenreEntity genre);

    @EntityGraph(attributePaths = {"author"}, type = EntityGraph.EntityGraphType.LOAD)
    List<BookEntity> findByAuthorAndGenres(AuthorEntity author, GenreEntity genre);
}
