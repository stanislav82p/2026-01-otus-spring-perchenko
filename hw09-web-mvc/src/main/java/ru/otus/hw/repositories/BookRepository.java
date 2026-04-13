package ru.otus.hw.repositories;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {

    @EntityGraph(attributePaths = {"author", "genres"}, type = EntityGraph.EntityGraphType.LOAD)
    @Override
    Optional<Book> findById(Long id);

    @EntityGraph(attributePaths = {"author"}, type = EntityGraph.EntityGraphType.LOAD)
    @Override
    List<Book> findAll();

    @EntityGraph(attributePaths = {"author"}, type = EntityGraph.EntityGraphType.LOAD)
    List<Book> findByAuthor(Author author);

    @EntityGraph(attributePaths = {"author"}, type = EntityGraph.EntityGraphType.LOAD)
    List<Book> findByGenres(Genre genre);

    @EntityGraph(attributePaths = {"author"}, type = EntityGraph.EntityGraphType.LOAD)
    List<Book> findByAuthorAndGenres(Author author, Genre genre);
}
