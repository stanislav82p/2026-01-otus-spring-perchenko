package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.entity.AuthorEntity;
import ru.otus.hw.models.entity.BookEntity;
import ru.otus.hw.models.dto.BookDto;
import ru.otus.hw.models.entity.GenreEntity;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.GenreRepository;
import ru.otus.hw.services.localization.LocalizedMessagesService;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.springframework.util.CollectionUtils.isEmpty;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {
    private final AuthorRepository authorRepository;

    private final GenreRepository genreRepository;

    private final BookRepository bookRepository;

    private final LocalizedMessagesService messageService;

    @Transactional(readOnly = true)
    @Override
    public BookDto findById(long id) {
        Optional<BookEntity> optBook = bookRepository.findById(id);
        if (optBook.isPresent()) {
            return BookDto.fromEntity(optBook.get());
        } else {
            throw new EntityNotFoundException(
                    "Book with ID %d not found".formatted(id),
                    messageService.getMessage("book-not-found")
            );
        }
    }

    @Transactional(readOnly = true)
    @Override
    public List<BookDto> findAll() {
        return bookRepository.findAll().stream().map(BookDto::fromEntity).toList();
    }

    @Transactional(readOnly = true)
    @Override
    public List<BookDto> findAllOfAuthor(long authorId) {
        return bookRepository.findByAuthor(AuthorEntity.forId(authorId))
                .stream()
                .map(BookDto::fromEntity)
                .toList();
    }

    @Override
    public List<BookDto> findAllOfGenre(long genreId) {
        return bookRepository.findByGenres(GenreEntity.forId(genreId))
                .stream()
                .map(BookDto::fromEntity)
                .toList();
    }

    @Override
    public List<BookDto> findAllOfAuthorAndGenre(long authorId, long genreId) {
        return bookRepository.findByAuthorAndGenres(AuthorEntity.forId(authorId), GenreEntity.forId(genreId))
                .stream()
                .map(BookDto::fromEntity)
                .toList();
    }

    @Override
    public BookDto insert(String title, long authorId, Set<Long> genresIds) {
        return save(0, title, authorId, genresIds);
    }

    @Override
    public BookDto update(long id, String title, long authorId, Set<Long> genresIds) {
        return save(id, title, authorId, genresIds);
    }

    @Transactional
    @Override
    public void deleteById(long id) {
        bookRepository.deleteById(id);
    }

    @Transactional
    private BookDto save(long id, String title, long authorId, Set<Long> genresIds) {
        if (isEmpty(genresIds)) {
            throw new IllegalArgumentException("Genres ids must not be null");
        }
        if (id > 0) {
            bookRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(
                    "Book with id %d not found for update".formatted(id),
                    messageService.getMessage("book-not-found"))
            );
        }
        var author = authorRepository.findById(authorId).orElseThrow(() -> new EntityNotFoundException(
                "Author with id %d not found".formatted(authorId),
                messageService.getMessage("author-not-found")));
        var genres = genreRepository.findByIdIn(genresIds);
        if (isEmpty(genres) || genresIds.size() != genres.size()) {
            throw new EntityNotFoundException(
                    "One or all genres with ids %s not found".formatted(genresIds),
                    messageService.getMessage("genres-not-found"));
        }
        var bookToSave = new BookEntity(id, title, author, genres);
        BookEntity savedBook = bookRepository.save(bookToSave);
        return BookDto.fromEntity(savedBook);
    }
}
