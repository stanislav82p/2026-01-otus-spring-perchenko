package ru.otus.hw.pages;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;
import ru.otus.hw.models.dto.BookDto;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.ReaderService;

@Controller
public class BookController {

    private final BookService bookService;

    private final ReaderService readerService;

    @Autowired
    public BookController(BookService bookService, ReaderService readerService) {
        this.bookService = bookService;
        this.readerService = readerService;
    }


    @GetMapping("/books")
    public ModelAndView listAllBooks(Authentication auth) {
        var username = auth.getName();
        var user = readerService.getByUsername(username);
        ModelAndView mv = new ModelAndView();
        mv.setViewName("books");
        mv.addObject("current_user_name", user.getFullName());
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
