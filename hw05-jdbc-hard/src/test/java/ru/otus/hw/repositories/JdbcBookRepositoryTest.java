package ru.otus.hw.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.otus.hw.converters.AuthorConverter;
import ru.otus.hw.converters.BookConverter;
import ru.otus.hw.converters.GenreConverter;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе Jdbc для работы с книгами ")
@JdbcTest
@Import({JdbcBookRepository.class, JdbcGenreRepository.class,
        JdbcAuthorRepository.class, AuthorConverter.class, GenreConverter.class, BookConverter.class})
class JdbcBookRepositoryTest {

    @Autowired
    private BookRepository bookRepo;

    @Autowired
    private AuthorRepository authorRepo;

    @Autowired
    private GenreRepository genreRepo;

    private List<Author> dbAuthors;

    private List<Genre> dbGenres;

    private List<Book> dbBooks;

    @BeforeEach
    void setUp() {
        dbAuthors = getDbAuthors();
        dbGenres = getDbGenres();
        dbBooks = getDbBooks(dbAuthors, dbGenres);
    }

    @DisplayName("Должен загружать список всех авторов")
    @Test
    void shouldReturnCorrectAuthorList(@Autowired AuthorConverter converter) {
        List<Author> actualAuthors = authorRepo.findAll();

        assertThat(actualAuthors).containsExactlyElementsOf(dbAuthors);
        actualAuthors.stream().map(converter::authorToString).forEach(System.out::println);
    }

    @DisplayName("Должен загружать автора по id")
    @ParameterizedTest
    @MethodSource("getDbAuthors")
    void shouldReturnCorrectAuthorById(Author expectedAuthor) {
        Optional<Author> optAuthor = authorRepo.findById(expectedAuthor.getId());

        assertThat(optAuthor).isPresent().get().usingRecursiveComparison().isEqualTo(expectedAuthor);
        System.out.println("shouldReturnCorrectAuthorById(): "+optAuthor.get());
    }

    @DisplayName("Должен вернуть нескольких авторов по заданным id")
    @Test
    void shouldReturnExpectedAuthors() {
        Set<Long> expectedIds = Set.of(1L, 3L);
        Map<Long, Author> actualAuthors = authorRepo.findByIds(expectedIds);

        expectedIds.forEach(id -> {
            assertThat(actualAuthors.get(id)).isNotNull().extracting(Author::getId).isEqualTo(id);
        });
    }

    @DisplayName("Не должен загружать автора по неверному id")
    @Test
    void shouldNotReturnAuthorForWrongId() {
        Optional<Author> actualAuthor = authorRepo.findById(10);
        assertThat(actualAuthor).isNotPresent();
    }

    @DisplayName("Должен загружать список всех жанров")
    @Test
    void shouldReturnCorrectGenreList(@Autowired GenreConverter converter) {
        List<Genre> actualGenres = genreRepo.findAll();

        assertThat(actualGenres)
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactlyElementsOf(dbGenres);
        actualGenres.stream().map(converter::genreToString).forEach(System.out::println);
    }

    @DisplayName("Должен загружать жанр по id")
    @Test
    void shouldReturnCorrectGenresByIds() {
        Set<Genre> expectedGenres = Set.of(dbGenres.get(1), dbGenres.get(3), dbGenres.get(4));

        Set<Long> ids = expectedGenres.stream().map(Genre::getId).collect(Collectors.toSet());
        Set<Genre> actualGenres = genreRepo.findAllByIds(ids);

        assertThat(actualGenres)
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactlyInAnyOrderElementsOf(expectedGenres);
    }

    @DisplayName("должен загружать книгу по id")
    @ParameterizedTest
    @MethodSource("getDbBooks")
    void shouldReturnCorrectBookById(Book expectedBook) {
        var actualBook = bookRepo.findById(expectedBook.getId());
        assertThat(actualBook).isPresent()
                .get()
                .usingRecursiveComparison()
                .isEqualTo(expectedBook);
    }

    @DisplayName("должен загружать список всех книг")
    @Test
    void shouldReturnCorrectBooksList(@Autowired BookConverter converter) {
        var actualBooks = bookRepo.findAll();
        var expectedBooks = dbBooks;

        assertThat(actualBooks)
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactlyElementsOf(expectedBooks);

        actualBooks.stream().map(converter::bookToString).forEach(System.out::println);
    }

    @DisplayName("должен сохранять новую книгу")
    @Test
    void shouldSaveNewBook() {
        Book expectedBook = new Book(0, "BookTitle_100500", dbAuthors.get(0),
                Set.of(dbGenres.get(0), dbGenres.get(2)));

        Book returnedBook = bookRepo.save(expectedBook);

        Optional<Book> foundBook = bookRepo.findById(returnedBook.getId());

        assertThat(returnedBook).isNotNull()
                .matches(book -> book.getId() > 0, "ID сохраненной книги почему-то 0");

        assertThat(foundBook)
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .isEqualTo(returnedBook);
    }

    @DisplayName("должен сохранять измененную книгу")
    @Test
    void shouldSaveUpdatedBook() {
        Book expectedBook = new Book(1L, "BookTitle_10500", dbAuthors.get(2),
                Set.of(dbGenres.get(4), dbGenres.get(5)));

        Optional<Book> originalSavedBook = bookRepo.findById(expectedBook.getId());

        assertThat(originalSavedBook)
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .isNotEqualTo(expectedBook);

        var returnedBook = bookRepo.save(expectedBook);
        assertThat(returnedBook).isNotNull().isEqualTo(expectedBook);

        Optional<Book> updatedBookFromDb = bookRepo.findById(returnedBook.getId());

        assertThat(updatedBookFromDb)
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .isEqualTo(expectedBook);
    }

    @DisplayName("должен удалять книгу по id ")
    @Test
    void shouldDeleteBook() {
        assertThat(bookRepo.findById(1L)).isPresent();
        bookRepo.deleteById(1L);
        assertThat(bookRepo.findById(1L)).isEmpty();
    }

    private static List<Author> getDbAuthors() {
        return IntStream.range(1, 4).boxed()
                .map(id -> new Author(id, "Author_" + id))
                .toList();
    }

    private static List<Genre> getDbGenres() {
        return IntStream.range(1, 7).boxed()
                .map(id -> new Genre(id, "Genre_" + id))
                .toList();
    }

    private static List<Book> getDbBooks(List<Author> dbAuthors, List<Genre> dbGenres) {
        return IntStream.range(1, 4).boxed()
                .map(id -> new Book(id,
                        "BookTitle_" + id,
                        dbAuthors.get(id - 1),
                        new HashSet<>(dbGenres.subList((id - 1) * 2, (id - 1) * 2 + 2))
                ))
                .toList();
    }

    private static List<Book> getDbBooks() {
        var dbAuthors = getDbAuthors();
        var dbGenres = getDbGenres();
        return getDbBooks(dbAuthors, dbGenres);
    }
}