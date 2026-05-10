package ru.otus.hw.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.hw.models.dto.AuthorDto;
import ru.otus.hw.services.AuthorService;

import java.util.List;

@RestController
public class AuthorApiController {

    private final AuthorService authorService;

    @Autowired
    public AuthorApiController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @GetMapping(path = "/api/library/authors")
    public List<AuthorDto> getAll() {
        return authorService.findAll();
    }
}
