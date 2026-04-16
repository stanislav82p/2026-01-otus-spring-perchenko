package ru.otus.hw.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.models.Reader;
import ru.otus.hw.repositories.BookRepositoryImpl;
import ru.otus.hw.repositories.CommentRepositoryImpl;
import ru.otus.hw.repositories.ReaderRepositoryImpl;
import ru.otus.hw.utils.EntityId;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Сервис для комментариев должен")
@DataJpaTest
@Import({CommentServiceImpl.class, CommentRepositoryImpl.class, BookRepositoryImpl.class, ReaderRepositoryImpl.class})
@Transactional(propagation = Propagation.NEVER)
public class CommentServiceTest {

    private static final long BOOK_ID_2 = 2L;
    private static final long READER_ID_1 = 1L;

    @Autowired
    private CommentService commService;

    @DisplayName("Должен загружать все комментарии")
    @Test
    void mustLoadAll() {
        List<Comment> comments = commService.findAll();
        assertThat(comments.size()).isEqualTo(8);
        assertThat(comments)
                .allMatch(it -> it.getReader() != null)
                .allMatch(it -> it.getBook().getAuthor() != null);
    }

    @DisplayName("Должен загружать все комментарии для книги")
    @Test
    void mustLoadAllCommentsForBook() {
        List<Comment> comments = commService.findAllForBook(EntityId.forValue(BOOK_ID_2));

        assertThat(comments.size()).isEqualTo(5);
        assertThat(comments)
                .allMatch(it -> it.getReader() != null)
                .allMatch(it -> it.getBook().getAuthor() != null)
                .allMatch(it -> it.getBook().getId() == BOOK_ID_2)
                .allMatch(it -> !it.getBook().getGenres().isEmpty());
    }

    @DisplayName("Должен загружать все комментарии от читателя для определенной книги")
    @Test
    void mustLoadAllCommentsFromReaderForBook() {
        EntityId<Book> bookId = EntityId.forValue(BOOK_ID_2);
        EntityId<Reader> readerId = EntityId.forValue(READER_ID_1);

        List<Comment> comments = commService.findAllForBookFromReader(bookId, readerId);

        assertThat(comments.size()).isEqualTo(2);
        assertThat(comments)
                .allMatch(it -> it.getReader() != null)
                .allMatch(it -> it.getBook().getAuthor() != null)
                .allMatch(it -> !it.getBook().getGenres().isEmpty())
                .allMatch(it ->
                ((it.getBook().getId() == bookId.id) && (it.getReader().getId() == readerId.id)));
    }

    @DisplayName("Должен создавать новый комментарий")
    @DirtiesContext
    @Test
    void mustCreateComment() {
        EntityId<Book> bookId = EntityId.forValue(BOOK_ID_2);
        EntityId<Reader> readerId = EntityId.forValue(READER_ID_1);
        Comment createdComm = commService.createComment(readerId, bookId, "Comment 100500");

        assertThat(createdComm.getId()).isGreaterThan(0);

        var found = commService.findById(EntityId.forValue(createdComm.getId()));

        assertThat(found).isPresent().get()
                .usingRecursiveComparison()
                .ignoringFieldsMatchingRegexes(".*hibernate_interceptor")
                .isEqualTo(createdComm);
    }

    @DisplayName("Должен удалять все комментарии читателя")
    @DirtiesContext
    @Test
    void mustRemoveAllCommentsFromReader() {
        int nDel = commService.deleteAllFromReader(EntityId.forValue(READER_ID_1));

        assertThat(nDel).isEqualTo(3);

        List<Comment> comments = commService.findAll();

        assertThat(comments.size()).isEqualTo(5);
        assertThat(comments).allMatch(it -> it.getReader().getId() != READER_ID_1);
    }
}
