package ru.otus.hw.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RootController {

    public static final String BASE_URL = "/api/library";

    @GetMapping("/")
    public String root() {
        return "redirect:%s/books".formatted(BASE_URL);
    }
}
