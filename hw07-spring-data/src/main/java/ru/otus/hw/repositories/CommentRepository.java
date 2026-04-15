package ru.otus.hw.repositories;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import ru.otus.hw.models.entity.BookEntity;
import ru.otus.hw.models.entity.CommentEntity;
import ru.otus.hw.models.entity.ReaderEntity;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<CommentEntity, Long> {

    @EntityGraph(attributePaths = {"book", "reader"})
    @Override
    List<CommentEntity> findAll();

    @EntityGraph(attributePaths = {"book", "reader"})
    @Override
    Optional<CommentEntity> findById(Long id);

    @EntityGraph(attributePaths = {"book", "reader"})
    List<CommentEntity> findByBook(BookEntity book);

    @EntityGraph(attributePaths = {"book", "reader"})
    List<CommentEntity> findByReader(ReaderEntity reader);

    @EntityGraph(attributePaths = {"book", "reader"})
    List<CommentEntity> findByReaderAndBook(ReaderEntity reader, BookEntity book);

    @Modifying
    int deleteByReader(ReaderEntity reader);
}
