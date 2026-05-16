package ru.otus.hw.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.dto.BookDto;
import ru.otus.hw.models.dto.GenreDto;
import ru.otus.hw.models.entity.AuthorEntity;
import ru.otus.hw.models.entity.BookEntity;
import ru.otus.hw.models.entity.GenreEntity;
import ru.otus.hw.rest.response.ErrorResponseDto;
import ru.otus.hw.rest.response.GetBooksResponseDto;
import ru.otus.hw.sequrity.AppSecurityConfiguration;
import ru.otus.hw.sequrity.UserRole;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.localization.LocalizedMessagesService;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;

@DisplayName("API-контроллер для книг")
@WebMvcTest(BookApiController.class)
@Import({AppSecurityConfiguration.class})
public class BookApiControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockitoBean
    private BookService bookService;

    @MockitoBean
    private AuthorService authorService;

    @MockitoBean
    private LocalizedMessagesService messageService;

    private List<BookDto> mBooks;

    @BeforeEach
    void setUp() {
        mBooks = new ArrayList<>(6);
        for (long i = 1; i < 6; i ++) {
            var author = new AuthorEntity(i, "Author_" + i);
            var genre1 = new GenreEntity(i, "Genre_" + i);
            var genre2 = new GenreEntity(i + 1, "Genre_" + (i + 1));
            var book = new BookEntity(i, "Book_" + i, author, Set.of(genre1, genre2));

            mBooks.add(BookDto.fromEntity(book));
        }
    }

    @DisplayName("Должен возвращать все книги")
    @Test
    void shouldReturnCorrectBookList() throws Exception {
        given(bookService.findAll()).willReturn(mBooks);
        given(messageService.getMessage(eq("action-details"))).willReturn("Details");
        given(messageService.getMessage(eq("action-delete"))).willReturn("Delete");
        given(messageService.getMessage(eq("books-count"), any(), any(), any()))
                .willReturn("All books found: 6. authorId = 0, genreId = 0");

        var expectedResponse = new GetBooksResponseDto(
                mBooks,
                "All books found: 6. authorId = 0, genreId = 0",
                "Details",
                "Delete"
        );

        String expectedJson = mapper.writeValueAsString(expectedResponse);

        mvc.perform(MockMvcRequestBuilders.get("/api/library/books?author_id=0&genre_id=0").with(
                        user("usr1").authorities(UserRole.MODERATOR.getGrantedAuthorities())
                ))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedJson));
    }

    @DisplayName("Должен возвращать все книги автора")
    @Test
    void shouldReturnBooksForAuthor() throws Exception {
        var books = mBooks.stream().filter(it -> it.getAuthor().getId() == 2).toList();

        given(bookService.findAllOfAuthor(eq(2L))).willReturn(books);
        given(messageService.getMessage(eq("action-details"))).willReturn("Details");
        given(messageService.getMessage(eq("action-delete"))).willReturn("Delete");
        given(messageService.getMessage(eq("books-count"), any(), any(), any()))
                .willReturn("All books found: 1. authorId = 2, genreId = 0");

        var expectedResponse = new GetBooksResponseDto(
                books,
                "All books found: 1. authorId = 2, genreId = 0",
                "Details",
                "Delete"
        );

        String expectedJson = mapper.writeValueAsString(expectedResponse);

        mvc.perform(MockMvcRequestBuilders.get("/api/library/books?author_id=2&genre_id=0").with(
                        user("usr1").authorities(UserRole.MODERATOR.getGrantedAuthorities())
                ))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedJson));
    }

    @DisplayName("Должен возвращать все книги жанра")
    @Test
    void shouldReturnBooksForGenre() throws Exception {
        var books = mBooks.stream().filter(it ->
                it.getGenres().stream().map(GenreDto::getId).toList().contains(3L))
                .toList();

        given(bookService.findAllOfGenre(eq(3L))).willReturn(books);
        given(messageService.getMessage(eq("action-details"))).willReturn("Details");
        given(messageService.getMessage(eq("action-delete"))).willReturn("Delete");
        given(messageService.getMessage(eq("books-count"), any(), any(), any()))
                .willReturn("All books found: 2. authorId = 0, genreId = 3");

        var expectedResponse = new GetBooksResponseDto(
                books,
                "All books found: 2. authorId = 0, genreId = 3",
                "Details",
                "Delete"
        );

        String expectedJson = mapper.writeValueAsString(expectedResponse);

        mvc.perform(MockMvcRequestBuilders.get("/api/library/books?author_id=0&genre_id=3").with(
                        user("usr1").authorities(UserRole.MODERATOR.getGrantedAuthorities())
                ))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedJson));
    }

    @DisplayName("Должен возвращать все книги по автору И жанру")
    @Test
    void shouldReturnBooksForAuthorAndGenre() throws Exception {
        var books = mBooks.stream().filter(it ->
                (it.getAuthor().getId() == 2) && it.getGenres().stream().map(GenreDto::getId).toList().contains(3L))
                .toList();

        given(bookService.findAllOfAuthorAndGenre(eq(2L), eq(3L))).willReturn(books);
        given(messageService.getMessage(eq("action-details"))).willReturn("Details");
        given(messageService.getMessage(eq("action-delete"))).willReturn("Delete");
        given(messageService.getMessage(eq("books-count"), any(), any(), any()))
                .willReturn("All books found: 1. authorId = 2, genreId = 3");

        var expectedResponse = new GetBooksResponseDto(
                books,
                "All books found: 1. authorId = 2, genreId = 3",
                "Details",
                "Delete"
        );

        String expectedJson = mapper.writeValueAsString(expectedResponse);

        mvc.perform(MockMvcRequestBuilders.get("/api/library/books?author_id=2&genre_id=3").with(
                        user("usr1").authorities(UserRole.MODERATOR.getGrantedAuthorities())
                ))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedJson));
    }

    private final long BOOK_ID_1 = 1;

    @DisplayName("Должен возвращать книгу по ID")
    @Test
    void shouldReturnBookById() throws Exception {

        given(bookService.findById(eq(BOOK_ID_1))).willReturn(mBooks.get(0));

        String expectedJson = mapper.writeValueAsString(mBooks.get(0));

        mvc.perform(MockMvcRequestBuilders.get("/api/library/books/"+BOOK_ID_1).with(
                        user("usr1").authorities(UserRole.MODERATOR.getGrantedAuthorities())
                ))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedJson));
    }

    @DisplayName("Должен возвращать ошибку, если книга не найдена")
    @Test
    void shouldReturnErrorOnBookNotFound() throws Exception {
        var ex = new EntityNotFoundException("Book with ID %d not found".formatted(BOOK_ID_1), "Book not found");
        given(bookService.findById(anyLong())).willThrow(ex);

        var expectedResponse = new ErrorResponseDto(ex.getMessage(), ex.getUserMessage());

        String expectedJson = mapper.writeValueAsString(expectedResponse);

        mvc.perform(MockMvcRequestBuilders.get("/api/library/books/100500").with(
                        user("usr1").authorities(UserRole.MODERATOR.getGrantedAuthorities())
                ))
                .andExpect(status().isNotFound())
                .andExpect(content().json(expectedJson));
    }

    @DisplayName("Должен удалять книгу")
    @Test
    void shouldDeleteBook() throws Exception {
        mvc.perform(MockMvcRequestBuilders.delete("/api/library/books/"+BOOK_ID_1).with(
                        user("usr1").authorities(UserRole.MODERATOR.getGrantedAuthorities())
                ))
                .andExpect(status().isOk())
                .andExpect(content().string(""));

        verify(bookService, times(1)).deleteById(eq(BOOK_ID_1));
    }

    @DisplayName("Должен возвращать 302 без авторизации и редирект на логин")
    @Test
    void shouldRedirectWhenNoAuth() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/api/library/books?author_id=0&genre_id=0"))
                .andExpect(status().isFound())
                .andExpect(header().string("Location", "http://localhost/login"));

        mvc.perform(MockMvcRequestBuilders.get("/api/library/books/"+BOOK_ID_1))
                .andExpect(status().isFound())
                .andExpect(header().string("Location", "http://localhost/login"));

        mvc.perform(MockMvcRequestBuilders.delete("/api/library/books/"+BOOK_ID_1))
                .andExpect(status().isFound())
                .andExpect(header().string("Location", "http://localhost/login"));
    }
}
