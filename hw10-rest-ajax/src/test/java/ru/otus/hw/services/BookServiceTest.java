package ru.otus.hw.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.dto.AuthorDto;
import ru.otus.hw.models.dto.BookDto;
import ru.otus.hw.services.localization.LocalizedMessagesService;
import ru.otus.hw.services.localization.LocalizedMessagesServiceImpl;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;

@DisplayName("Сервис для книг должен")
@DataJpaTest
@Import({BookServiceImpl.class, LocalizedMessagesServiceImpl.class})
@Transactional(propagation = Propagation.NEVER)
public class BookServiceTest {

    private static final long BOOK_ID_2 = 2L;
    private static final long AUTHOR_ID_2 = 2L;

    @Autowired
    private BookService bookService;

    @MockitoBean
    private LocalizedMessagesService messageService;

    @DisplayName("Должен загружать книгу по ID")
    @Test
    void mustLoadBookById() {
        BookDto book = bookService.findById(BOOK_ID_2);

        assertThat(book).extracting(BookDto::getId).isEqualTo(BOOK_ID_2);
        assertThat(book)
                .extracting(BookDto::getAuthor)
                .extracting(AuthorDto::getId)
                .isEqualTo(AUTHOR_ID_2);
        assertThat(book)
                .extracting(BookDto::getGenres)
                .extracting(Set::size)
                .isEqualTo(2);
    }

    @DisplayName("Должен загружать все книги")
    @Test
    void mustLoadAllBooks() {
        List<BookDto> books = bookService.findAll();

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
        BookDto book = bookService.findById(BOOK_ID_2);

        var userMessage = "Book not found";
        given(messageService.getMessage(eq("book-not-found"))).willReturn(userMessage);

        bookService.deleteById(BOOK_ID_2);

        var exception = Assertions.assertThrows(
                EntityNotFoundException.class,
                () -> bookService.findById(BOOK_ID_2)
        );
        assertThat(exception.getMessage()).isEqualTo("Book with ID %d not found".formatted(BOOK_ID_2));
        assertThat(exception.getUserMessage()).isEqualTo(userMessage);
    }
}
