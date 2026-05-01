package ru.otus.hw.pages;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;
import ru.otus.hw.models.dto.AuthorDto;
import ru.otus.hw.models.dto.BookDto;
import ru.otus.hw.models.dto.GenreDto;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.CommentService;
import ru.otus.hw.services.GenreService;

import java.util.List;

@Controller
public class BookController {

    private final BookService bookService;

    private final AuthorService authorService;

    private final GenreService genreService;

    private final CommentService commentService;

    @Autowired
    public BookController(
            BookService bookService, AuthorService authorService,
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
        List<AuthorDto> authors = authorService.findAll();
        List<GenreDto> genres = genreService.findAll();

        ModelAndView mv = new ModelAndView();
        mv.setViewName("books");
        mv.addObject("authors", authors);
        mv.addObject("genres", genres);
        return mv;
    }

    @GetMapping("/books/{id}")
    public ModelAndView bookDetails(@PathVariable("id") long bookId) {
        BookDto b = bookService.findById(bookId);

        ModelAndView mv = new ModelAndView();
        mv.setViewName("bookDetails");
        mv.addObject("book_id", b.getId());
        return mv;
    }

    @GetMapping("/books/{id}/delete")
    public ModelAndView requestDeleteBook(@PathVariable("id") long bookId, Model model) {
        BookDto b = bookService.findById(bookId);
        var mv = new ModelAndView();
        mv.setViewName("bookDeleteConfirm");
        mv.addObject("book_id", b.getId());
        return mv;
    }

    @GetMapping("/books/{id}/editor")
    public ModelAndView openBookEditor(@PathVariable("id") long bookId) {
        var mv = new ModelAndView();
        mv.setViewName("bookEditor");
        mv.addObject("book_id", bookId);
        return mv;
    }

    @GetMapping("/books/creator")
    public String openBookCreator() {
        return "bookCreator";
    }
}
