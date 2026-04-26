package ru.otus.hw.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.ModelAndView;
import ru.otus.hw.controller.dto.BookCreationDto;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.models.Genre;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.CommentService;
import ru.otus.hw.services.GenreService;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static ru.otus.hw.controller.RootController.BASE_URL;

@Controller
@RequestMapping(BASE_URL)
public class BookController {

    private final BookService bookService;

    private final AuthorService authorService;

    private final GenreService genreService;

    private final CommentService commentService;

    @Autowired
    public BookController(
            BookService bookService,
            AuthorService authorService,
            GenreService genreService,
            CommentService commentService
    ) {
        this.bookService = bookService;
        this.authorService = authorService;
        this.genreService = genreService;
        this.commentService = commentService;
    }


    @GetMapping("/books")
    public ModelAndView listAllBooks() {
        List<Book> books = bookService.findAll();
        List<Author> authors = authorService.findAll();
        List<Genre> genres = genreService.findAll();

        ModelAndView mv = new ModelAndView();
        mv.setViewName("books");
        mv.addObject("books", books);
        mv.addObject("authors", authors);
        mv.addObject("genres", genres);
        mv.addObject("selected_author_id", 0);
        mv.addObject("selected_genre_id", 0);
        return mv;
    }

    @GetMapping(path = "/books", params = {"author_id", "genre_id"})
    public ModelAndView listBooksOfAuthorGenre(
            @RequestParam(value = "author_id", defaultValue = "0") long authorId,
            @RequestParam(value = "genre_id", defaultValue = "0") long genreId
    ) {
        List<Book> books;
        ModelAndView mv = new ModelAndView();
        if (authorId <= 0 && genreId <= 0) {
            mv.setViewName("redirect:%s/books".formatted(BASE_URL));
            return mv;
        } else if (genreId <= 0) {
            books = bookService.findAllOfAuthor(authorId);
        } else if (authorId <= 0) {
            books = bookService.findAllOfGenre(genreId);
        } else {
            books = bookService.findAllOfAuthorAndGenre(authorId, genreId);
        }

        mv.setViewName("books");
        mv.addObject("books", books);
        mv.addObject("authors", authorService.findAll());
        mv.addObject("genres",  genreService.findAll());
        mv.addObject("selected_author_id", authorId);
        mv.addObject("selected_genre_id", genreId);
        return mv;
    }

    @GetMapping("/books/{id}")
    public ModelAndView bookDetails(@PathVariable("id") long bookId) {
        Book b = bookService.findById(bookId);
        List<Comment> comments = commentService.findAllForBook(bookId)
                .stream()
                .sorted((c1, c2) -> (int)(c1.getDate().getTime() - c2.getDate().getTime()))
                .toList();

        ModelAndView mv = new ModelAndView();
        mv.setViewName("bookDetails");
        mv.addObject("book", b);
        mv.addObject("comments", comments);
        return mv;
    }

    @GetMapping("/books/{id}/delete")
    public String requestDeleteBook(@PathVariable("id") long bookId, Model model) {
        Book b = bookService.findById(bookId);
        model.addAttribute("book", b);
        return "bookDeleteConfirm";
    }

    @DeleteMapping("/books/{id}")
    public String deleteBook(@PathVariable("id") long bookId) {
        bookService.deleteById(bookId);

        return "redirect:%s/books".formatted(BASE_URL);
    }

    @GetMapping("/books/{id}/editor")
    public ModelAndView openBookEditor(@PathVariable("id") long bookId) {
        Book b = bookService.findById(bookId);
        List<Author> authors = authorService.findAll();
        List<Genre> genres = genreService.findAll();

        var dto = new BookCreationDto(
                b.getAuthor().getId(),
                b.getTitle(),
                b.getGenres().stream().map(Genre::getId).collect(Collectors.toSet())
        );

        var mv = new ModelAndView();
        mv.setViewName("bookEditor");
        mv.addObject("book", b);
        mv.addObject("authors", authors);
        mv.addObject("genres", genres);
        mv.addObject("edtBook", dto);
        return mv;
    }

    @PostMapping("/books/{id}/editor")
    public String saveBook(
            @PathVariable("id") long bookId,
            @Valid @ModelAttribute("edtBook") BookCreationDto edtBook,
            BindingResult bindingResult,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            Book b = bookService.findById(bookId);
            List<Author> authors = authorService.findAll();
            List<Genre> genres = genreService.findAll();
            model.addAttribute("book", b);
            model.addAttribute("authors", authors);
            model.addAttribute("genres", genres);
            return "bookEditor";
        }

        bookService.update(bookId, edtBook.getBookTitle(), edtBook.getAuthorId(), edtBook.getGenreIds());

        return "redirect:%s/books/%d".formatted(BASE_URL, bookId);
    }

    @GetMapping("/books/creator")
    public ModelAndView openBookCreator() {
        List<Author> authors = authorService.findAll();
        List<Genre> genres = genreService.findAll();

        var dto = new BookCreationDto(0, "", Set.of(1L));

        var mv = new ModelAndView();
        mv.setViewName("bookCreator");
        mv.addObject("authors", authors);
        mv.addObject("genres", genres);
        mv.addObject("edtBook", dto);
        return mv;
    }

    @PostMapping("/books/creator")
    public String createBook(
            @Valid @ModelAttribute("edtBook") BookCreationDto edtBook,
            BindingResult bindingResult,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            List<Author> authors = authorService.findAll();
            List<Genre> genres = genreService.findAll();
            model.addAttribute("authors", authors);
            model.addAttribute("genres", genres);
            return "bookCreator";
        }
        bookService.insert(edtBook.getBookTitle(), edtBook.getAuthorId(), edtBook.getGenreIds());

        return "redirect:%s/books".formatted(BASE_URL);
    }
}
