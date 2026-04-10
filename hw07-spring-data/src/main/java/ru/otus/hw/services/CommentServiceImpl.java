package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.models.Reader;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;
import ru.otus.hw.repositories.ReaderRepository;
import ru.otus.hw.utils.EntityId;

import java.sql.Date;
import java.util.List;

@RequiredArgsConstructor
@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepo;

    private final BookRepository bookRepo;

    private final ReaderRepository readerRepo;

    @Transactional(readOnly = true)
    @Override
    public Comment findById(Long commentId) {
        var optComment = commentRepo.findById(commentId);

        if (optComment.isPresent()) {
            var comment = optComment.get();
            // !!! Пинаю лэйзи-коллекцию на втором уровне, чтобы загрузилась.
            // Просто транзакция не помогает. ХЗ как сделать правильно
            comment.getBook().getGenres().isEmpty();
            comment.getBook().getAuthor().getFullName();

            return comment;
        } else {
            throw new EntityNotFoundException("Комментарий с ID %d не найден".formatted(commentId));
        }
    }

    @Transactional(readOnly = true)
    @Override
    public  List<Comment> findAll() {
        var comments = commentRepo.findAll();

        // !!! Пинаю лэйзи-коллекцию на втором уровне, чтобы загрузилась.
        // Просто транзакция не помогает. ХЗ как сделать правильно
        comments.forEach(it -> {
            it.getBook().getGenres().isEmpty();
            it.getBook().getAuthor().getFullName();
        });


        return comments;
    }

    @Transactional(readOnly = true)
    @Override
    public List<Comment> findAllForBook(Long bookId) {
        var book = bookRepo.findById(bookId);
        if (book.isEmpty()) {
            throw new EntityNotFoundException("The book with ID %s was not found".formatted(bookId));
        }

        return commentRepo.findByBook(book.get());
    }

    @Transactional(readOnly = true)
    @Override
    public List<Comment> findAllForBookFromReader(Long bookId, Long readerId) {
        var book = bookRepo.findById(bookId).orElseThrow(
                () -> new EntityNotFoundException("The book with ID %s was not found".formatted(bookId))
        );

        var reader = readerRepo.findById(readerId).orElseThrow(
                () -> new EntityNotFoundException("The reader with ID %s was not found".formatted(readerId))
        );

        return commentRepo.findByReaderAndBook(reader, book);
    }

    @Transactional
    @Override
    public void deleteById(Long commentId) {
        commentRepo.deleteById(commentId);
    }

    @Transactional
    @Override
    public int deleteAllFromReader(Long readerId) {
        return commentRepo.deleteByReader(Reader.forId(readerId));
    }

    @Transactional
    @Override
    public Comment createComment(EntityId<Reader> readerId, EntityId<Book> bookId, String text) {
        var book = bookRepo.findById(bookId.id).orElseThrow(
                () -> new EntityNotFoundException("The book with ID %s was not found".formatted(bookId))
        );

        var reader = readerRepo.findById(readerId.id).orElseThrow(
                () -> new EntityNotFoundException("The reader with ID %s was not found".formatted(readerId))
        );

        return commentRepo.saveAndFlush(
                new Comment(0, book, reader, text, new Date(System.currentTimeMillis()))
        );
    }

    @Transactional
    @Override
    public Comment updateComment(long commentId, String text) {
        Comment originalComment = commentRepo.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("Комментарий с ID %d не найден".formatted(commentId)));

        Comment updComment = originalComment.toBuilder()
                .text(text)
                .date(new Date(System.currentTimeMillis()))
                .build();


        Comment savedComment = commentRepo.saveAndFlush(updComment);
        savedComment.getBook().getGenres().isEmpty();
        savedComment.getBook().getAuthor().getFullName();

        return savedComment;
    }
}
