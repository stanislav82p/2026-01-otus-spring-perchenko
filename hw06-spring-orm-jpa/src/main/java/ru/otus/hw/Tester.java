package ru.otus.hw;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.converters.ModelConverter;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.models.Genre;
import ru.otus.hw.models.Reader;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;
import ru.otus.hw.repositories.GenreRepository;

import java.util.Optional;
import java.util.Set;

@RequiredArgsConstructor
@Component
public class Tester {

    private final ConfigurableApplicationContext context;

    @Transactional
    public void checkBooks(ConfigurableApplicationContext context) {
        var bookRepo = context.getBean(BookRepository.class);
        var bookConv = ModelConverter.getConverterFor(context, Book.class);

        System.out.println("==============================================================");

        System.out.println("\r\n----> find book by id (2)");
        Optional<Book> optBook = bookRepo.findById(2);
        if (optBook.isPresent()) {
            System.out.println(bookConv.convertToString(optBook.get()));
        } else {
            System.out.println("not found");
        }

        System.out.println("\r\n----> find all books");
        bookRepo.findAll().forEach((Book b) -> {
            System.out.println("---  " + bookConv.convertToString(b));
        });

        System.out.println("\r\n----> delete book for IS - (1)");

        boolean isOk = bookRepo.deleteById(1);
        System.out.println("book deleted (1) - " + isOk);
    }

    @Transactional
    public void checkComments(ConfigurableApplicationContext context) {
        var commentRepo = context.getBean(CommentRepository.class);
        var commentConverter = ModelConverter.getConverterFor(context, Comment.class);

        System.out.println("==============================================================");
        System.out.println("\r\n----> find all comments");
        commentRepo.findAll().forEach((Comment c) -> {
            System.out.println("---  " + commentConverter.convertToString(c));
        });

        System.out.println("\r\n----> find comment by id (4)");
        Optional<Comment> optComment = commentRepo.findById(4);
        if (optComment.isPresent()) {
            System.out.println(commentConverter.convertToString(optComment.get()));
        } else {
            System.out.println("not found");
        }

        System.out.println("\r\n----> find all comments for book (1)");
        commentRepo.findAllForBook(1).forEach((Comment c) -> {
            System.out.println("---  " + commentConverter.convertToString(c));
        });

        System.out.println("\r\n----> find all comments for reader (3)");
        commentRepo.findAllFromReader(3).forEach((Comment c) -> {
            System.out.println("---  " + commentConverter.convertToString(c));
        });


        var isOk = commentRepo.update(1, "111111111");
        System.out.println("--- updated (1) = " + isOk);

        isOk = commentRepo.deleteById(2);
        System.out.println("--- deleted (2) = " + isOk);

        isOk = commentRepo.deleteById(100500);
        System.out.println("--- deleted (100500) = " + isOk);

        //var book = context.getBean(BookRepository.class).findById(3);
        var book = new Book(3, "sdfsdf", new Author(5, "sdfgdsf"), Set.of());
        var reader = new Reader(4, "Reader_4");

        var comment = commentRepo.createComment(book, reader, "qwerty1234567890");
        System.out.println("created comment = " + commentConverter.convertToString(comment));

        comment = commentRepo.createComment(book, reader, "sfgsdffg sdfg sdfsd fsdfgdfg ssdf");
        System.out.println("created comment = " + commentConverter.convertToString(comment));
    }

    public void checkAuthor(ConfigurableApplicationContext context) {
        var authorRepo = context.getBean(AuthorRepository.class);
        var authConverter = ModelConverter.getConverterFor(context, Author.class);

        System.out.println("==============================================================");
        System.out.println("\r\n----> find all authors");
        authorRepo.findAll().forEach((Author a) -> {
            System.out.println(authConverter.convertToString(a));
        });
        System.out.println("\r\n----> find author by id (2)");
        Optional<Author> optAuthor = authorRepo.findById(2);
        if (optAuthor.isPresent()) {
            System.out.println(authConverter.convertToString(optAuthor.get()));
        } else {
            System.out.println("not found");
        }

        System.out.println("\r\n----> find authors by ids (1, 3, 4)");
        authorRepo.findByIds(Set.of(1L, 3L, 4L)).values().forEach((Author a) -> {
            System.out.println(authConverter.convertToString(a));
        });
    }

    public void checkGenre(ConfigurableApplicationContext context) {
        var genreRepo = context.getBean(GenreRepository.class);
        var genreConverter = ModelConverter.getConverterFor(context, Genre.class);

        System.out.println("==============================================================");
        System.out.println("\r\n----> find all genres");
        genreRepo.findAll().forEach((Genre g) -> {
            System.out.println(genreConverter.convertToString(g));
        });
        System.out.println("\r\n----> find genre by id (2)");
        Optional<Genre> optGenre = genreRepo.findById(2);
        if (optGenre.isPresent()) {
            System.out.println(genreConverter.convertToString(optGenre.get()));
        } else {
            System.out.println("not found");
        }

        System.out.println("\r\n----> find genres by ids (1, 3)");
        genreRepo.findAllByIds(Set.of(1L, 3L)).forEach((Genre g) -> {
            System.out.println(genreConverter.convertToString(g));
        });
    }
}
