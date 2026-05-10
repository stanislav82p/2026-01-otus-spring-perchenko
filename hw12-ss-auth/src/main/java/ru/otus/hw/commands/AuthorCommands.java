package ru.otus.hw.commands;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.hw.converters.ModelConverter;
import ru.otus.hw.models.dto.AuthorDto;
import ru.otus.hw.services.AuthorService;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@ShellComponent
public class AuthorCommands {

    private final AuthorService authorService;

    private final ModelConverter<AuthorDto> authorConverter;

    @ShellMethod(value = "Find all authors", key = "aa")
    public String findAllAuthors() {
        return authorService.findAll().stream()
                .map(authorConverter::convertToString)
                .collect(Collectors.joining("," + System.lineSeparator()));
    }

    @ShellMethod(value = "Find author by ID", key = "aid")
    public String findAuthorById(long id) {
        var author = authorService.findById(id);
        return authorConverter.convertToString(author);
    }

    @ShellMethod(value = "Find all authors", key = "aids")
    public String findAuthorsByIds(Long... ids) {
        Set<Long> authorIds = new HashSet<>(Arrays.asList(ids));
        return authorService.findByIds(authorIds).values().stream()
                .map(authorConverter::convertToString)
                .collect(Collectors.joining("," + System.lineSeparator()));
    }
}
