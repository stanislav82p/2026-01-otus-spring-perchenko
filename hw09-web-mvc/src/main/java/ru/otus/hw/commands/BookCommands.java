package ru.otus.hw.commands;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.otus.hw.converters.ModelConverter;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.dto.BookDto;
import ru.otus.hw.services.BookService;

import java.util.Set;
import java.util.stream.Collectors;

@SuppressWarnings({"SpellCheckingInspection", "unused"})
@RequiredArgsConstructor
@ShellComponent
public class BookCommands {

    private final BookService bookService;

    private final ModelConverter<BookDto> bookConverter;

    @ShellMethod(value = "Find all books", key = "ab")
    public String findAllBooks() {
        return bookService.findAll().stream()
                .map(bookConverter::convertToString)
                .collect(Collectors.joining("," + System.lineSeparator()));
    }

    @ShellMethod(value = "Find book by id", key = "bbid")
    public String findBookById(long id) {
        var book = bookService.findById(id);
        return bookConverter.convertToString(book);
    }

    @ShellMethod(value = "Find books of Author", key = "bbauthor")
    public String findBooksOfAuthor(@ShellOption(value = { "--author-id" }, help = "ID автора") long authorId) {
        return bookService.findAllOfAuthor(authorId).stream()
                .map(bookConverter::convertToString)
                .collect(Collectors.joining("," + System.lineSeparator()));
    }

    // bins newBook 1 1,6
    @ShellMethod(value = "Insert book", key = "bins")
    public String insertBook(String title, long authorId, Set<Long> genreIds) {
        var savedBook = bookService.insert(title, authorId, genreIds);
        return bookConverter.convertToString(savedBook);
    }

    // bupd 4 editedBook 3 2,5
    @ShellMethod(value = "Update book", key = "bupd")
    public String updateBook(long id, String title, long authorId, Set<Long> genreIds) {
        var savedBook = bookService.update(id, title, authorId, genreIds);
        return bookConverter.convertToString(savedBook);
    }

    // bdel 4
    @ShellMethod(value = "Delete book by id", key = "bdel")
    public void deleteBook(long id) {
        bookService.deleteById(id);
    }
}
