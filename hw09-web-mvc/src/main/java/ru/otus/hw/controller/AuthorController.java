package ru.otus.hw.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.otus.hw.models.Author;
import ru.otus.hw.services.AuthorService;

import java.util.List;

import static ru.otus.hw.controller.RootController.BASE_URL;

@Controller
@RequestMapping(BASE_URL)
public class AuthorController {

    private final AuthorService authorService;

    @Autowired
    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @GetMapping("/authors")
    public String listAllAuthors(Model model) {
        List<? extends Author> authors = authorService.findAll();
        model.addAttribute("authors", authors);
        return "authors";
    }
}
