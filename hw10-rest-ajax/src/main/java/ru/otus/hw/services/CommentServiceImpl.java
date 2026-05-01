package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.dto.CommentLightDto;
import ru.otus.hw.models.entity.BookEntity;
import ru.otus.hw.models.entity.CommentEntity;
import ru.otus.hw.models.entity.ReaderEntity;
import ru.otus.hw.models.dto.CommentDto;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;
import ru.otus.hw.repositories.ReaderRepository;
import ru.otus.hw.services.localization.LocalizedMessagesService;
import ru.otus.hw.utils.EntityId;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepo;

    private final BookRepository bookRepo;

    private final ReaderRepository readerRepo;

    private final LocalizedMessagesService messageService;

    @Transactional(readOnly = true)
    @Override
    public CommentDto findById(Long commentId) {
        var optComment = commentRepo.findById(commentId);

        if (optComment.isPresent()) {
            return CommentDto.fromEntity(optComment.get());
        } else {
            throw new EntityNotFoundException(
                    "Комментарий с ID %d не найден".formatted(commentId),
                    messageService.getMessage("comment-not-found")
            );
        }
    }

    @Transactional(readOnly = true)
    @Override
    public  List<CommentLightDto> findAll() {
        return commentRepo.findAll().stream().map(CommentLightDto::fromEntity).toList();
    }

    @Transactional(readOnly = true)
    @Override
    public List<CommentDto> findAllForBook(Long bookId) {
        var book = bookRepo.findById(bookId);
        if (book.isEmpty()) {
            throw new EntityNotFoundException(
                    "The book with ID %s was not found".formatted(bookId),
                    messageService.getMessage("book-not-found")
            );
        }

        return commentRepo.findByBook(book.get()).stream().map(CommentDto::fromEntity).toList();
    }

    public List<CommentDto> findAllFromReader(Long readerId) {
        Optional<ReaderEntity> reader = readerRepo.findById(readerId);
        if (reader.isEmpty()) {
            throw new EntityNotFoundException(
                    "The reader with ID %s was not found".formatted(readerId),
                    messageService.getMessage("reader-not-found")
            );
        }

        return commentRepo.findByReader(reader.get()).stream().map(CommentDto::fromEntity).toList();
    }

    @Transactional(readOnly = true)
    @Override
    public List<CommentDto> findAllForBookFromReader(Long bookId, Long readerId) {
        var book = bookRepo.findById(bookId).orElseThrow(
                () -> new EntityNotFoundException(
                        "The book with ID %s was not found".formatted(bookId),
                        messageService.getMessage("book-not-found")
                )
        );

        var reader = readerRepo.findById(readerId).orElseThrow(
                () -> new EntityNotFoundException(
                        "The reader with ID %s was not found".formatted(readerId),
                        messageService.getMessage("reader-not-found")
                )
        );

        return commentRepo.findByReaderAndBook(reader, book)
                .stream()
                .map(CommentDto::fromEntity)
                .toList();
    }

    @Transactional
    @Override
    public void deleteById(Long commentId) {
        commentRepo.deleteById(commentId);
    }

    @Transactional
    @Override
    public int deleteAllFromReader(Long readerId) {
        return commentRepo.deleteByReader(ReaderEntity.forId(readerId));
    }

    @Transactional
    @Override
    public CommentDto createComment(EntityId<ReaderEntity> readerId, EntityId<BookEntity> bookId, String text) {
        var book = bookRepo.findById(bookId.id).orElseThrow(
                () -> new EntityNotFoundException(
                        "The book with ID %d was not found".formatted(bookId),
                        messageService.getMessage("book-not-found")
                )
        );

        var reader = readerRepo.findById(readerId.id).orElseThrow(
                () -> new EntityNotFoundException(
                        "The reader with ID %d was not found".formatted(readerId),
                        messageService.getMessage("reader-not-found")
                )
        );

        CommentEntity createdComment = commentRepo.saveAndFlush(
                new CommentEntity(0, book, reader, text, new Date(System.currentTimeMillis()))
        );
        return CommentDto.fromEntity(createdComment);
    }

    @Transactional
    @Override
    public CommentLightDto updateComment(long commentId, String text) {
        CommentEntity originalComment = commentRepo.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Комментарий с ID %d не найден".formatted(commentId),
                        messageService.getMessage("comment-not-found")
                ));

        CommentEntity updComment = originalComment.toBuilder()
                .text(text)
                .date(new Date(System.currentTimeMillis()))
                .build();


        CommentEntity savedComment = commentRepo.saveAndFlush(updComment);
        return CommentLightDto.fromEntity(savedComment);
    }
}
