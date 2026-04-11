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
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepo;

    private final BookRepository bookRepo;

    private final ReaderRepository readerRepo;

    @Transactional(readOnly = true)
    @Override
    public Optional<Comment> findById(EntityId<Comment> commentId) {
        var comment = commentRepo.findById(commentId.id);

        comment.ifPresent(it -> {
            it.getBook().getGenres().isEmpty();
            it.getBook().getAuthor().getFullName();
        });

        return comment;
    }

    @Transactional(readOnly = true)
    @Override
    public  List<Comment> findAll() {
        return commentRepo.findAll();
    }

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
    public List<Comment> findAllFromReader(EntityId<Reader> readerId) {
        var reader = readerRepo.findById(readerId.id);
        if (reader.isEmpty()) {
            throw new EntityNotFoundException("The reader with ID %s was not found".formatted(readerId));
        }

        return commentRepo.findAllFromReader(readerId.id);
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
        return commentRepo.deleteById(commentId.id) == 1;
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

        var comment = commentRepo.createComment(book, reader, text);

        return comment;
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
