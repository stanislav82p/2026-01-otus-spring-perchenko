package ru.otus.hw.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.repositories.AuthorRepositoryImpl;
import ru.otus.hw.repositories.BookRepositoryImpl;
import ru.otus.hw.repositories.GenreRepositoryImpl;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Сервис для книг должен")
@DataJpaTest
@Import({BookServiceImpl.class, BookRepositoryImpl.class, AuthorRepositoryImpl.class, GenreRepositoryImpl.class})
@Transactional(propagation = Propagation.NEVER)
public class BookServiceTest {

    private static final long BOOK_ID_2 = 2L;
    private static final long AUTHOR_ID_2 = 2L;

    @Autowired
    private BookService bookService;

    @DisplayName("Должен загружать книгу по ID")
    @Test
    void mustLoadBookById() {
        Optional<Book> book = bookService.findById(BOOK_ID_2);

        assertThat(book).isPresent().get().extracting(Book::getId).isEqualTo(BOOK_ID_2);
        assertThat(book).isPresent().get()
                .extracting(Book::getAuthor)
                .extracting(Author::getId)
                .isEqualTo(AUTHOR_ID_2);
        assertThat(book).isPresent().get()
                .extracting(Book::getGenres)
                .extracting(Set::size)
                .isEqualTo(2);
    }

    @DisplayName("Должен загружать все книги")
    @Test
    void mustLoadAllBooks() {
        List<Book> books = bookService.findAll();

        assertThat(books.size()).isEqualTo(3);

        books.forEach(it -> {
            assertThat(it.getAuthor().getId()).isGreaterThan(0);
            assertThat(it.getGenres()).isNotEmpty();
        });
    }

    @DisplayName("Должен удалять книгу по ID")
    @DirtiesContext
    @Test
    void mustDeleteBookById() {
        Optional<Book> book = bookService.findById(BOOK_ID_2);
        assertThat(book).isPresent();

        bookService.deleteById(BOOK_ID_2);

        book = bookService.findById(BOOK_ID_2);
        assertThat(book).isEmpty();
    }
}
