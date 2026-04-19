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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.ModelAndView;
import ru.otus.hw.controller.dto.CommentCreationDto;
import ru.otus.hw.models.dto.BookDto;
import ru.otus.hw.models.dto.ReaderDto;
import ru.otus.hw.models.entity.BookEntity;
import ru.otus.hw.models.entity.ReaderEntity;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.CommentService;
import ru.otus.hw.services.ReaderService;
import ru.otus.hw.utils.EntityId;

import java.util.List;

import static ru.otus.hw.controller.RootController.BASE_URL;

@Controller
@RequestMapping(BASE_URL)
public class CommentController {

    private final BookService bookService;

    private final CommentService commentService;

    private final ReaderService readerService;

    @Autowired
    public CommentController(BookService bookService, CommentService commentService, ReaderService readerService) {
        this.bookService = bookService;
        this.commentService = commentService;
        this.readerService = readerService;
    }

    @GetMapping("/books/{id}/commenter")
    public ModelAndView bookDetails(@PathVariable("id") long bookId) {
        List<ReaderDto> readers = readerService.findAll();
        BookDto book = bookService.findById(bookId);

        ModelAndView mv = new ModelAndView();
        mv.setViewName("commenter");
        mv.addObject("book", book);
        mv.addObject("readers", readers);
        mv.addObject("commentCreation", new CommentCreationDto(0, null));
        return mv;
    }

    @PostMapping("/comments")
    public String createComment(
            @Valid @ModelAttribute("commentCreation") CommentCreationDto commentCreation,
            BindingResult bindingResult,
            @RequestParam("book_id") long bookId,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            List<ReaderDto> readers = readerService.findAll();
            BookDto book = bookService.findById(bookId);
            model.addAttribute("book", book);
            model.addAttribute("readers", readers);
            return "commenter";
        }

        EntityId<ReaderEntity> rId = EntityId.forValue(commentCreation.getReaderId());
        EntityId<BookEntity> bId = EntityId.forValue(bookId);

        commentService.createComment(rId, bId, commentCreation.getCommentText());

        return "redirect:%s/books/%d".formatted(BASE_URL, bookId);
    }
}
