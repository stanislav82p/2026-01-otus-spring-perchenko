package ru.otus.hw.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Сервис для книг должен")
@DataJpaTest
@Import({BookServiceImpl.class})
@Transactional(propagation = Propagation.NEVER)
public class BookServiceTest {

    private static final long BOOK_ID_2 = 2L;
    private static final long AUTHOR_ID_2 = 2L;

    @Autowired
    private BookService bookService;

    @DisplayName("Должен загружать книгу по ID")
    @Test
    void mustLoadBookById() {
        Book book = bookService.findById(BOOK_ID_2);

        assertThat(book).extracting(Book::getId).isEqualTo(BOOK_ID_2);
        assertThat(book)
                .extracting(Book::getAuthor)
                .extracting(Author::getId)
                .isEqualTo(AUTHOR_ID_2);
        assertThat(book)
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
        Book book = bookService.findById(BOOK_ID_2);

        bookService.deleteById(BOOK_ID_2);

        var exception = Assertions.assertThrows(
                EntityNotFoundException.class,
                () -> bookService.findById(BOOK_ID_2)
        );
        assertThat(exception.getMessage()).isEqualTo("Book with ID %d not found".formatted(BOOK_ID_2));
    }
}
