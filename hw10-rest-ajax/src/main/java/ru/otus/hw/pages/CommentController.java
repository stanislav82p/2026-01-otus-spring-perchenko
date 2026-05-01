package ru.otus.hw.pages;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;
import ru.otus.hw.models.dto.BookDto;
import ru.otus.hw.services.BookService;

@Controller
public class CommentController {

    private final BookService bookService;

    @Autowired
    public CommentController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/books/{id}/commenter")
    public ModelAndView createComment(@PathVariable("id") long bookId) {
        BookDto book = bookService.findById(bookId);

        ModelAndView mv = new ModelAndView();
        mv.setViewName("commenter");
        mv.addObject("book_id", book.getId());
        return mv;
    }
}
