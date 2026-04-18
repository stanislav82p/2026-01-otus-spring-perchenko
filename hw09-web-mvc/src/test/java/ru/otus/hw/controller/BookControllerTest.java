package ru.otus.hw.controller;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.otus.hw.controller.dto.BookCreationDto;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.models.Genre;
import ru.otus.hw.models.entity.*;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.CommentService;
import ru.otus.hw.services.GenreService;

import java.sql.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static ru.otus.hw.controller.RootController.BASE_URL;

@WebMvcTest(BookController.class)
public class BookControllerTest {

    public static final long BOOK_ID_1 = 1L;
    public static final long AUTHOR_ID_1 = 1L;
    public static final long GENRE_ID_4 = 4L;

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private BookService bookService;

    @MockitoBean
    private AuthorService authorService;

    @MockitoBean
    private GenreService genreService;

    @MockitoBean
    private CommentService commentService;

    private final List<Book> books = List.of(
            new BookEntity(1, "BookTitle_1", new AuthorEntity(1, "Author_1"), Set.of(new GenreEntity(1, "Genre_1"), new GenreEntity(2, "Genre_2"))),
            new BookEntity(2, "BookTitle_2", new AuthorEntity(2, "Author_2"), Set.of(new GenreEntity(3, "Genre_3"), new GenreEntity(4, "Genre_4"))),
            new BookEntity(3, "BookTitle_3", new AuthorEntity(3, "Author_3"), Set.of(new GenreEntity(5, "Genre_5"), new GenreEntity(6, "Genre_6"))),
            new BookEntity(4, "BookTitle_4", new AuthorEntity(1, "Author_1"), Set.of(new GenreEntity(2, "Genre_2"), new GenreEntity(4, "Genre_4")))
    );

    private final List<Author> authors = List.of(
            new AuthorEntity(1, "Author_1"), new AuthorEntity(2, "Author_2"),
            new AuthorEntity(3, "Author_3"), new AuthorEntity(4, "Author_4")
    );

    private final List<Genre> genres = List.of(
            new GenreEntity(1, "Genre_1"), new GenreEntity(2, "Genre_2"),
            new GenreEntity(3, "Genre_3"), new GenreEntity(4, "Genre_4"),
            new GenreEntity(5, "Genre_5"), new GenreEntity(6, "Genre_6")
    );

    private final List<Comment> comments = List.of(
            new CommentEntity(1, (BookEntity) books.get(0), new ReaderEntity(1, "Reader_1"), "comment-1-1", new Date(0)),
            new CommentEntity(2, (BookEntity) books.get(0), new ReaderEntity(2, "Reader_2"), "comment-1-2", new Date(0)),
            new CommentEntity(3, (BookEntity) books.get(0), new ReaderEntity(3, "Reader_3"), "comment-1-3", new Date(0))
    );

    @Test
    void testGetAll() throws Exception {
        given(bookService.findAll()).willReturn(books);
        given(authorService.findAll()).willReturn(authors);
        given(genreService.findAll()).willReturn(genres);

        mvc.perform(MockMvcRequestBuilders.get(BASE_URL+"/books"))
                .andExpect(MockMvcResultMatchers.view().name("books"))
                .andExpect(MockMvcResultMatchers.model().attribute("books", books))
                .andExpect(MockMvcResultMatchers.model().attribute("authors", authors))
                .andExpect(MockMvcResultMatchers.model().attribute("genres", genres))
                .andExpect(MockMvcResultMatchers.model().attribute("selected_author_id", 0))
                .andExpect(MockMvcResultMatchers.model().attribute("selected_genre_id", 0));
    }

    @Test
    void testGetByAuthor() throws Exception {
        var expected = List.of(books.get(0), books.get(3));
        given(bookService.findAllOfAuthor(eq(AUTHOR_ID_1))).willReturn(expected);
        given(authorService.findAll()).willReturn(authors);
        given(genreService.findAll()).willReturn(genres);

        mvc.perform(MockMvcRequestBuilders.get(BASE_URL+"/books?author_id=1&genre_id=0"))
                .andExpect(MockMvcResultMatchers.view().name("books"))
                .andExpect(MockMvcResultMatchers.model().attribute("books", expected))
                .andExpect(MockMvcResultMatchers.model().attribute("authors", authors))
                .andExpect(MockMvcResultMatchers.model().attribute("genres", genres))
                .andExpect(MockMvcResultMatchers.model().attribute("selected_author_id", 1L))
                .andExpect(MockMvcResultMatchers.model().attribute("selected_genre_id", 0L));

        Mockito.verify(bookService, times(1)).findAllOfAuthor(AUTHOR_ID_1);
    }

    @Test
    void testGetByGenre() throws Exception {
        List<Book> expectedBooks = List.of(books.get(1), books.get(3));
        given(bookService.findAllOfGenre(eq(GENRE_ID_4))).willReturn(expectedBooks);
        given(authorService.findAll()).willReturn(authors);
        given(genreService.findAll()).willReturn(genres);

        mvc.perform(MockMvcRequestBuilders.get(BASE_URL+"/books?author_id=0&genre_id=4"))
                .andExpect(MockMvcResultMatchers.view().name("books"))
                .andExpect(MockMvcResultMatchers.model().attribute("books", expectedBooks))
                .andExpect(MockMvcResultMatchers.model().attribute("authors", authors))
                .andExpect(MockMvcResultMatchers.model().attribute("genres", genres))
                .andExpect(MockMvcResultMatchers.model().attribute("selected_author_id", 0L))
                .andExpect(MockMvcResultMatchers.model().attribute("selected_genre_id", 4L));

        Mockito.verify(bookService, times(1)).findAllOfGenre(GENRE_ID_4);
    }

    @Test
    void testGetByAuthorAndGenre() throws Exception {
        var expectedBooks = List.of(books.get(3));
        given(bookService.findAllOfAuthorAndGenre(eq(AUTHOR_ID_1), eq(GENRE_ID_4))).willReturn(expectedBooks);
        given(authorService.findAll()).willReturn(authors);
        given(genreService.findAll()).willReturn(genres);

        mvc.perform(MockMvcRequestBuilders.get(BASE_URL+"/books?author_id=1&genre_id=4"))
                .andExpect(MockMvcResultMatchers.view().name("books"))
                .andExpect(MockMvcResultMatchers.model().attribute("books", expectedBooks))
                .andExpect(MockMvcResultMatchers.model().attribute("authors", authors))
                .andExpect(MockMvcResultMatchers.model().attribute("genres", genres))
                .andExpect(MockMvcResultMatchers.model().attribute("selected_author_id", 1L))
                .andExpect(MockMvcResultMatchers.model().attribute("selected_genre_id", 4L));

        Mockito.verify(bookService, times(1)).findAllOfAuthorAndGenre(AUTHOR_ID_1, GENRE_ID_4);
    }

    @Test
    void testGetBookDetails() throws Exception {
        given(bookService.findById(BOOK_ID_1)).willReturn(books.get(0));
        given(commentService.findAllForBook(eq(BOOK_ID_1))).willReturn(comments);

        mvc.perform(MockMvcRequestBuilders.get(BASE_URL+"/books/1"))
                .andExpect(MockMvcResultMatchers.view().name("bookDetails"))
                .andExpect(MockMvcResultMatchers.model().attribute("book", books.get(0)))
                .andExpect(MockMvcResultMatchers.model().attribute("comments", comments));

        Mockito.verify(bookService, times(1)).findById(BOOK_ID_1);
        Mockito.verify(commentService, times(1)).findAllForBook(BOOK_ID_1);
    }

    @Test
    void testRequestDelete() throws Exception {
        given(bookService.findById(BOOK_ID_1)).willReturn(books.get(0));

        mvc.perform(MockMvcRequestBuilders.get(BASE_URL+"/books/1/delete"))
                .andExpect(MockMvcResultMatchers.view().name("bookDeleteConfirm"))
                .andExpect(MockMvcResultMatchers.model().attribute("book", books.get(0)));
    }

    @Test
    void testDeleteBook() throws Exception {
        mvc.perform(MockMvcRequestBuilders.delete(BASE_URL+"/books/1"))
                .andExpect(MockMvcResultMatchers.view().name("redirect:%s/books".formatted(BASE_URL)));
    }

    @Test
    void testOpenEditor() throws Exception {
        var originalBook = books.get(0);
        given(bookService.findById(BOOK_ID_1)).willReturn(originalBook);
        given(authorService.findAll()).willReturn(authors);
        given(genreService.findAll()).willReturn(genres);

        var expectedEdt = new BookCreationDto(
                originalBook.getId(),
                originalBook.getTitle(),
                originalBook.getGenres().stream().map(Genre::getId).collect(Collectors.toSet())
        );

        mvc.perform(MockMvcRequestBuilders.get(BASE_URL+"/books/1/editor"))
                .andExpect(MockMvcResultMatchers.view().name("bookEditor"))
                .andExpect(MockMvcResultMatchers.model().attribute("book", originalBook))
                .andExpect(MockMvcResultMatchers.model().attribute("edtBook", expectedEdt))
                .andExpect(MockMvcResultMatchers.model().attribute("authors", authors))
                .andExpect(MockMvcResultMatchers.model().attribute("genres", genres));

        Mockito.verify(bookService, times(1)).findById(BOOK_ID_1);
    }


}
