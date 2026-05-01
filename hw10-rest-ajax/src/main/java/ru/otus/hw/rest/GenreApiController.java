package ru.otus.hw.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.hw.models.dto.GenreDto;
import ru.otus.hw.services.GenreService;

import java.util.List;

@RestController
public class GenreApiController {

    private final GenreService genreService;

    @Autowired
    public GenreApiController(GenreService genreService) {
        this.genreService = genreService;
    }

    @GetMapping(path = "/api/library/genres")
    public List<GenreDto> getAll() {
        return genreService.findAll();
    }
}
