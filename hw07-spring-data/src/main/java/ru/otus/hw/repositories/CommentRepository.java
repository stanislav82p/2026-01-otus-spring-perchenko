package ru.otus.hw.repositories;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.models.Reader;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @EntityGraph(attributePaths = {"book", "reader"})
    @Override
    List<Comment> findAll();

    @EntityGraph(attributePaths = {"book", "reader"})
    @Override
    Optional<Comment> findById(Long id);

    @EntityGraph(attributePaths = {"book", "reader"})
    List<Comment> findByBook(Book book);

    @EntityGraph(attributePaths = {"book", "reader"})
    List<Comment> findByReader(Reader reader);

    @EntityGraph(attributePaths = {"book", "reader"})
    List<Comment> findByReaderAndBook(Reader reader, Book book);

    @Modifying
    int deleteByReader(Reader reader);
}
