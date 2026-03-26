package ru.otus.hw.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
public class JdbcBookRepository implements BookRepository {

    public static final String TABLE_NAME = "books";

    public static final String ID = "id";

    public static final String TITLE = "title";

    public static final String AUTHOR_ID = "author_id";

    private final GenreRepository genreRepo;

    private final AuthorRepository authorRepo;

    private final NamedParameterJdbcOperations namedParamJdbc;

    private final TransactionTemplate transactionTemplate;

    @Autowired
    public JdbcBookRepository(
            GenreRepository genreRepo,
            AuthorRepository authorRepo,
            NamedParameterJdbcOperations namedParamJdbc,
            PlatformTransactionManager transactionManager
    ) {
        this.genreRepo = genreRepo;
        this.authorRepo = authorRepo;
        this.namedParamJdbc = namedParamJdbc;
        this.transactionTemplate = new TransactionTemplate(transactionManager);
    }

    @Override
    public Optional<Book> findById(long id) {
        var params = Map.of("b_id", id);
        var sql = "SELECT %1$s, %2$s, %3$s FROM %4$s WHERE %1$s = :b_id".formatted(ID, TITLE, AUTHOR_ID, TABLE_NAME);
        Book rawBook = namedParamJdbc.query(sql, params, new BookResultSetExtractor());
        if (rawBook == null) {
            return Optional.empty();
        }

        Author author = getAuthorOrThrow(rawBook.getAuthor().getId());

        Set<Long> genreIds = getGenreRelationsForBook(rawBook).stream()
                .map(BookGenreRelation::genreId).collect(Collectors.toSet());

        Set<Genre> genres = genreRepo.findAllByIds(genreIds);

        var theBook = Book.builder()
                .id(rawBook.getId())
                .title(rawBook.getTitle())
                .author(author)
                .genres(genres)
                .build();

        return Optional.of(theBook);
    }

    private Author getAuthorOrThrow(long authorId) {
        return authorRepo.findById(authorId).orElseThrow(
                () -> new DataIntegrityViolationException("Author with id %d not found".formatted(authorId))
        );
    }

    @Override
    public List<Book> findAll() {
        var genres = genreRepo.findAll();
        var books = getAllBooksWithoutGenres();
        var relations = getAllGenreRelations();
        return mergeBooksInfo(books, genres, relations);
    }

    @Override
    public Book save(Book book) {
        if (book.getId() == 0) {
            return insert(book);
        } else {
            update(book);
            return book;
        }
    }

    @Override
    public void deleteById(long id) {
        transactionTemplate.executeWithoutResult(status -> {
            var params = Map.of("book_id", id);
            var sql = "DELETE FROM %1$s WHERE %2$s = :book_id".formatted(TABLE_NAME, ID);
            int nDel = namedParamJdbc.update(sql, params);
            System.out.println("deleted book - " + nDel);
            if (nDel > 0) {
                nDel = namedParamJdbc.update("DELETE FROM books_genres WHERE book_id = :book_id", params);
                System.out.println("Deleted relations - " + nDel);
            }
        });
    }

    private List<Book> getAllBooksWithoutGenres() {
        var sql = "SELECT %1$s, %2$s, %3$s FROM %4$s ORDER BY %2$s ASC".formatted(ID, TITLE, AUTHOR_ID, TABLE_NAME);
        List<Book> rawBooks = namedParamJdbc.query(sql, new BookRowMapper());

        // Если у нас в базе есть авторы без книг, то мы не будем тянуть лишних авторов
        Set<Long> authorIds = rawBooks.stream().map(it -> it.getAuthor().getId()).collect(Collectors.toSet());
        Map<Long, Author> authorMap = authorRepo.findByIds(authorIds);

        return rawBooks.stream().map(book -> {
            var authorId = book.getAuthor().getId();
            return book.withAuthor(authorMap.get(authorId));
        }).toList();
    }

    private List<BookGenreRelation> getAllGenreRelations() {
        var sql = "SELECT genre_id, book_id FROM books_genres";
        List<BookGenreRelation> relations = namedParamJdbc.query(sql, (ResultSet rs, int rowNum) ->
                new BookGenreRelation(rs.getLong("book_id"), rs.getLong("genre_id"))
        );
        return relations;
    }

    // Переделал сигнатуру этого метода. Книги теперь immutable. Да и логика, основанная на побочных
    // эффектах, не есть хорошо
    private List<Book> mergeBooksInfo(
            List<Book> booksWithoutGenres,
            List<Genre> genres,
            List<BookGenreRelation> relations
    ) {
        List<Book> resultBooks = new ArrayList<>(booksWithoutGenres.size());

        Map<Long, Genre> genreMap = new HashMap<>();
        for (Genre g : genres) {
            genreMap.put(g.getId(), g);
        }

        for (Book b : booksWithoutGenres) {
            Set<Genre> bookGenres = relations.stream()
                    .filter(r -> r.bookId == b.getId())
                    .map(r -> genreMap.get(r.genreId))
                    .collect(Collectors.toSet());

            resultBooks.add(b.withGenres(bookGenres));
        }

        return resultBooks;
    }

    private Book insert(Book book) {
        return transactionTemplate.execute(state -> {
            var keyHolder = new GeneratedKeyHolder();

            var params = Map.of("title", book.getTitle(), "authorId", book.getAuthor().getId());
            var paramSource = new MapSqlParameterSource().addValues(params);
            var sql = "INSERT INTO %1$s (%2$s, %3$s) VALUES (:title, :authorId)".formatted(TABLE_NAME, TITLE, AUTHOR_ID);
            namedParamJdbc.update(sql, paramSource, keyHolder);

            Book savedBook = book.withId(keyHolder.getKey().longValue());

            batchInsertGenresRelationsFor(savedBook);
            return savedBook;
        });
    }

    private void update(Book book) {
        transactionTemplate.executeWithoutResult(state -> {
            var params = Map.of(
                    "title", book.getTitle(),
                    "authorId", book.getAuthor().getId(),
                    "bookId", book.getId()
            );
            var sql = "UPDATE %1$s SET %2$s = :title, %3$s = :authorId WHERE %4$s = :bookId"
                    .formatted(TABLE_NAME, TITLE, AUTHOR_ID, ID);
            int nUpdated = namedParamJdbc.update(sql, params);
            if (nUpdated == 0) {
                throw new EntityNotFoundException("Не найдена книга с ID = " + book.getId());
            }

            namedParamJdbc.update("DELETE FROM books_genres WHERE book_id = :bid", Map.of("bid", book.getId()));

            batchInsertGenresRelationsFor(book);
        });
    }

    private void batchInsertGenresRelationsFor(Book book) {
        Map<String, Long>[] params = book.getGenres().stream()
                .map(genre -> Map.of("bookId", book.getId(), "genreId", genre.getId()))
                .toList()
                .toArray(new Map[0]);

        var sql = "INSERT INTO books_genres (book_id, genre_id) VALUES (:bookId, :genreId)";
        namedParamJdbc.batchUpdate(sql, params);
    }


    private Set<BookGenreRelation> getGenreRelationsForBook(Book book) {
        var params = Map.of("b_id", book.getId());
        var sql = "SELECT genre_id FROM books_genres WHERE book_id = :b_id";
        List<BookGenreRelation> relations = namedParamJdbc.query(sql, params, (ResultSet rs, int rowNum) ->
                new BookGenreRelation(book.getId(), rs.getLong(1))
        );
        return Set.copyOf(relations);
    }

    private static class BookRowMapper implements RowMapper<Book> {

        @Override
        public Book mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Book(
                    rs.getLong(ID),
                    rs.getString(TITLE),
                    new Author(
                            rs.getLong(AUTHOR_ID),
                            null
                    ),
                    Set.of()
            );
        }
    }

    @SuppressWarnings("ClassCanBeRecord")
    private static class BookResultSetExtractor implements ResultSetExtractor<Book> {

        @Override
        public Book extractData(ResultSet rs) throws SQLException, DataAccessException {
            if (rs.next()) {
                return new Book(
                        rs.getLong(ID),
                        rs.getString(TITLE),
                        new Author(
                                rs.getLong(AUTHOR_ID),
                                null
                        ),
                        Set.of()
                );
            }
            return null;
        }
    }

    private record BookGenreRelation(long bookId, long genreId) { }
}
