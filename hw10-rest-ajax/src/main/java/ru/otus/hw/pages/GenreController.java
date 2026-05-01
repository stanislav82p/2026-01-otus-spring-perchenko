package ru.otus.hw.pages;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class GenreController {

    @GetMapping("/genres")
    public String listAllAuthors() {
        return "genres";
    }
}
