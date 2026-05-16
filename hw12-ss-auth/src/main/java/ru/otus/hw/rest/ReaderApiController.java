package ru.otus.hw.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.hw.models.dto.ReaderDto;
import ru.otus.hw.rest.response.ProfileResponseDto;
import ru.otus.hw.services.ReaderService;

import java.util.List;

@RestController
public class ReaderApiController {

    private final ReaderService readerService;

    @Autowired
    public ReaderApiController(ReaderService readerService) {
        this.readerService = readerService;
    }

    @GetMapping(path = "/api/library/readers")
    public List<ReaderDto> getAll() {
        return readerService.findAll();
    }

    @GetMapping(path = "/api/library/readers/current")
    public ProfileResponseDto getCurrentReader() {
        var username = SecurityContextHolder.getContext().getAuthentication().getName();
        return readerService.getByUsername(username);
    }
}
