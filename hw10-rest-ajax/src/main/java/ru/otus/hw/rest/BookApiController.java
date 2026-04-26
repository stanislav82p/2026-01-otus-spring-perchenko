package ru.otus.hw.rest;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.hw.models.dto.BookDto;
import ru.otus.hw.rest.request.BookCreationDto;
import ru.otus.hw.rest.response.BookUpdateResponseDto;
import ru.otus.hw.rest.response.GetBooksResponseDto;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.localization.LocalizedMessagesService;

import java.util.List;

@RestController
public class BookApiController {

    private final BookService bookService;

    private final LocalizedMessagesService messageService;

    @Autowired
    public BookApiController(LocalizedMessagesService messageService, BookService bookService) {
        this.bookService = bookService;
        this.messageService = messageService;
    }

    @GetMapping(path = "/api/library/books", params = {"author_id", "genre_id"})
    public GetBooksResponseDto getBooks(
            @RequestParam(value = "author_id", defaultValue = "0") long authorId,
            @RequestParam(value = "genre_id", defaultValue = "0") long genreId
    ) {
        List<BookDto> books;
        if (authorId == 0 && genreId == 0) {
            books = bookService.findAll();
        } else if (authorId > 0 && genreId == 0) {
            books = bookService.findAllOfAuthor(authorId);
        } else if (authorId == 0 && genreId > 0) {
            books = bookService.findAllOfGenre(genreId);
        } else if (authorId > 0 && genreId > 0) {
            books = bookService.findAllOfAuthorAndGenre(authorId, genreId);
        } else {
            books = List.of();
        }

        String summary = messageService.getMessage("books-count", books.size(), authorId, genreId);
        String details = messageService.getMessage("action-details");
        String delete  = messageService.getMessage("action-delete");

        return new GetBooksResponseDto(books, summary, details, delete);
    }

    @GetMapping(path = "/api/library/books/{id}")
    public BookDto getBookById(@PathVariable("id") long bookId) {
        return bookService.findById(bookId);
    }

    @DeleteMapping(path = "api/library/books/{id}")
    public HttpEntity deleteBook(@PathVariable("id") long bookId) {
        bookService.deleteById(bookId);
        return HttpEntity.EMPTY;
    }

    @PutMapping(path = "/api/library/books/{id}")
    public ResponseEntity<BookUpdateResponseDto> updateBook(
            @PathVariable("id") long bookId,
            @Valid @RequestBody BookCreationDto dto,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            var errors = bindingResult.getAllErrors()
                    .stream()
                    .map(it -> new BookUpdateResponseDto.ErrorItem(it.getCodes()[0], it.getDefaultMessage()))
                    .toList();

            return ResponseEntity
                    .status(HttpStatus.UNPROCESSABLE_ENTITY)
                    .body(new BookUpdateResponseDto(null, errors));
        }
        BookDto updBook = bookService.update(bookId, dto.getBookTitle(), dto.getAuthorId(), dto.getGenreIds());
        return ResponseEntity.ok(new BookUpdateResponseDto(updBook, null));
    }

    @PostMapping(path = "/api/library/books")
    public ResponseEntity<BookUpdateResponseDto> createBook(
            @Valid @RequestBody BookCreationDto dto,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            var errors = bindingResult.getAllErrors()
                    .stream()
                    .map(it -> new BookUpdateResponseDto.ErrorItem(it.getCodes()[0], it.getDefaultMessage()))
                    .toList();

            return ResponseEntity
                    .status(HttpStatus.UNPROCESSABLE_ENTITY)
                    .body(new BookUpdateResponseDto(null, errors));
        }
        BookDto book = bookService.insert(dto.getBookTitle(), dto.getAuthorId(), dto.getGenreIds());
        return ResponseEntity.ok(new BookUpdateResponseDto(book, null));
    }
}
