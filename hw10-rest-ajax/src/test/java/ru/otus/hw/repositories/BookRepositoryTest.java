package ru.otus.hw.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.otus.hw.models.entity.AuthorEntity;
import ru.otus.hw.models.entity.BookEntity;
import ru.otus.hw.models.entity.GenreEntity;

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

    private List<BookEntity> dbBooks;

    @BeforeEach
    void setUp() {
        dbBooks = new ArrayList<>(6);
        for (long id = 1; id <= 3; id ++) {
            var book = em.find(BookEntity.class, id);
            var txtBook = book.toString();
            System.out.printf("\r\n---> Book (%d): %s", id, txtBook);
            em.detach(book);
            dbBooks.add(book);
        }
    }

    @DisplayName("должен загружать книгу по id")
    @Test
    void shouldReturnCorrectBookById() {
        for (BookEntity expectedBook : dbBooks) {
            Optional<BookEntity> actualBook = bookRepo.findById(expectedBook.getId());
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
        BookEntity bookToSave = new BookEntity(
                0,
                "BookTitle_10500",
                em.find(AuthorEntity.class, 2),
                Set.of(
                        em.find(GenreEntity.class, 4),
                        em.find(GenreEntity.class, 5)
                )
        );

        BookEntity returnedBook = bookRepo.save(bookToSave);

        assertThat(returnedBook)
                .isNotNull()
                .matches(book -> book.getId() > 0, "ID сохраненной книги почему-то 0");


        BookEntity originalBook = bookToSave.withId(returnedBook.getId());

        BookEntity foundBook = em.find(BookEntity.class, returnedBook.getId());

        assertThat(foundBook)
                .usingRecursiveComparison()
                .isEqualTo(originalBook);
    }

    @DisplayName("должен сохранять измененную книгу")
    @Test
    void shouldSaveUpdatedBook() {
        BookEntity expectedBook = new BookEntity(
                BOOK_ID_1,
                "BookTitle_10500",
                em.find(AuthorEntity.class, 2),
                Set.of(
                        em.find(GenreEntity.class, 4),
                        em.find(GenreEntity.class, 5)
                )
        );

        BookEntity originalSavedBook = em.find(BookEntity.class, expectedBook.getId());

        assertThat(originalSavedBook)
                .usingRecursiveComparison()
                .isNotEqualTo(expectedBook);
        em.detach(originalSavedBook);

        var returnedBook = bookRepo.save(expectedBook);

        assertThat(returnedBook)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(expectedBook);

        BookEntity updatedBookFromDb = em.find(BookEntity.class, expectedBook.getId());

        assertThat(updatedBookFromDb)
                .usingRecursiveComparison()
                .isEqualTo(expectedBook);
    }

    @DisplayName("должен выбрасывать исключение при попытке обновить несуществующую книгу")
    @Test
    void mustThrowExceptionOnUpdateNonExistedBook() {
        BookEntity book = new BookEntity(
                100500L,
                "BookTitle_10500",
                em.find(AuthorEntity.class, 2),
                Set.of(
                        em.find(GenreEntity.class, 4),
                        em.find(GenreEntity.class, 5)
                )
        );

        assertThatThrownBy(() -> bookRepo.save(book));
    }

    @DisplayName("должен удалять книгу по id ")
    @Test
    void shouldDeleteBook() {
        BookEntity book = em.find(BookEntity.class, BOOK_ID_1);
        em.detach(book);
        assertThat(book).isNotNull();

        bookRepo.deleteById(BOOK_ID_1);

        assertThat(em.find(BookEntity.class, BOOK_ID_1)).isNull();
    }
}
