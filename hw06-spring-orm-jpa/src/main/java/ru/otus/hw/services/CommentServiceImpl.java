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

import java.util.List;

@RequiredArgsConstructor
@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepo;

    private final BookRepository bookRepo;

    private final ReaderRepository readerRepo;

    @Transactional(readOnly = true)
    @Override
    public List<Comment> findAllForBook(EntityId<Book> bookId) {
        var book = bookRepo.findById(bookId.id);
        if (book.isEmpty()) {
            throw new EntityNotFoundException("The book with ID %s was not found".formatted(bookId));
        }

        return commentRepo.findAllForBook(bookId.id);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Comment> findAllForBookFromReader(EntityId<Book> bookId, EntityId<Reader> readerId) {
        var book = bookRepo.findById(bookId.id).orElseThrow(
                () -> new EntityNotFoundException("The book with ID %s was not found".formatted(bookId))
        );

        var reader = readerRepo.findById(readerId.id).orElseThrow(
                () -> new EntityNotFoundException("The reader with ID %s was not found".formatted(readerId))
        );

        return commentRepo.findAllFromReaderForBook(reader, book);
    }

    @Transactional
    @Override
    public boolean deleteById(EntityId<Comment> commentId) {
        return commentRepo.deleteById(commentId.id);
    }

    @Transactional
    @Override
    public int deleteAllFromReader(EntityId<Reader> readerId) {
        return commentRepo.deleteAllFromReader(readerId.id);
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

        return commentRepo.createComment(book, reader, text);
    }

    @Transactional
    @Override
    public Comment updateComment(EntityId<Comment> commentId, String text) {
        boolean isOk = commentRepo.update(commentId.id, text);
        if (!isOk) {
            throw new EntityNotFoundException("The comment with ID %s was not updated".formatted(commentId));
        }

        return commentRepo.findById(commentId.id).orElseThrow(
                () -> new EntityNotFoundException("The comment with ID %s was not found".formatted(commentId))
        );
    }
}
