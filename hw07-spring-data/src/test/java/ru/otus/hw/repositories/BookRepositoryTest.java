package ru.otus.hw.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Репозиторий на основе Spring ORM для работы с книгами ")
@DataJpaTest
public class BookRepositoryTest {

    private static final long BOOK_ID_1 = 1L;

    @Autowired
    TestEntityManager em;

    @Autowired
    private BookRepository bookRepo;

    private List<Book> dbBooks;

    @BeforeEach
    void setUp() {
        dbBooks = new ArrayList<>(6);
        for (long id = 1; id <= 3; id ++) {
            var book = em.find(Book.class, id);
            var txtBook = book.toString();
            System.out.printf("\r\n---> Book (%d): %s", id, txtBook);
            em.detach(book);
            dbBooks.add(book);
        }
    }

    @DisplayName("должен загружать книгу по id")
    @Test
    void shouldReturnCorrectBookById() {
        for (Book expectedBook : dbBooks) {
            Optional<Book> actualBook = bookRepo.findById(expectedBook.getId());
            assertThat(actualBook).isPresent()
                    .get()
                    .usingRecursiveComparison()
                    .isEqualTo(expectedBook);
        }
    }

    @DisplayName("должен загружать список всех книг")
    @Test
    void shouldReturnCorrectBooksList() {
        var actualBooks = bookRepo.findAll();
        var expectedBooks = dbBooks;

        assertThat(actualBooks)
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactlyElementsOf(expectedBooks);
    }

    @DisplayName("должен сохранять новую книгу")
    @Test
    void shouldSaveNewBook() {
        Book bookToSave = new Book(
                0,
                "BookTitle_10500",
                em.find(Author.class, 2),
                Set.of(
                        em.find(Genre.class, 4),
                        em.find(Genre.class, 5)
                )
        );

        Book returnedBook = bookRepo.save(bookToSave);

        assertThat(returnedBook)
                .isNotNull()
                .matches(book -> book.getId() > 0, "ID сохраненной книги почему-то 0");


        Book originalBook = bookToSave.withId(returnedBook.getId());

        Book foundBook = em.find(Book.class, returnedBook.getId());

        assertThat(foundBook)
                .usingRecursiveComparison()
                .isEqualTo(originalBook);
    }

    @DisplayName("должен сохранять измененную книгу")
    @Test
    void shouldSaveUpdatedBook() {
        Book expectedBook = new Book(
                BOOK_ID_1,
                "BookTitle_10500",
                em.find(Author.class, 2),
                Set.of(
                        em.find(Genre.class, 4),
                        em.find(Genre.class, 5)
                )
        );

        Book originalSavedBook = em.find(Book.class, expectedBook.getId());

        assertThat(originalSavedBook)
                .usingRecursiveComparison()
                .isNotEqualTo(expectedBook);
        em.detach(originalSavedBook);

        var returnedBook = bookRepo.save(expectedBook);

        assertThat(returnedBook)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(expectedBook);

        Book updatedBookFromDb = em.find(Book.class, expectedBook.getId());

        assertThat(updatedBookFromDb)
                .usingRecursiveComparison()
                .isEqualTo(expectedBook);
    }

    @DisplayName("должен выбрасывать исключение при попытке обновить несуществующую книгу")
    @Test
    void mustThrowExceptionOnUpdateNonExistedBook() {
        Book book = new Book(
                100500L,
                "BookTitle_10500",
                em.find(Author.class, 2),
                Set.of(
                        em.find(Genre.class, 4),
                        em.find(Genre.class, 5)
                )
        );

        assertThatThrownBy(() -> bookRepo.save(book));
    }

    @DisplayName("должен удалять книгу по id ")
    @Test
    void shouldDeleteBook() {
        Book book = em.find(Book.class, BOOK_ID_1);
        em.detach(book);
        assertThat(book).isNotNull();

        bookRepo.deleteById(BOOK_ID_1);

        assertThat(em.find(Book.class, BOOK_ID_1)).isNull();
    }
}
