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

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Сервис для комментариев должен")
@DataJpaTest
@Import({CommentServiceImpl.class})
@Transactional(propagation = Propagation.NEVER)
public class CommentServiceTest {

    private static final long BOOK_ID_2 = 2L;
    private static final long READER_ID_1 = 1L;

    @Autowired
    private CommentService commService;

    @DisplayName("Должен загружать все комментарии")
    @Test
    void mustLoadAll() {
        List<? extends Comment> comments = commService.findAll();
        assertThat(comments.size()).isEqualTo(8);
        assertThat(comments)
                .allMatch(it -> it.getReader() != null)
                .allMatch(it -> it.getBook().getAuthor() != null);
    }

    @DisplayName("Должен загружать все комментарии для книги")
    @Test
    void mustLoadAllCommentsForBook() {
        List<? extends Comment> comments = commService.findAllForBook(BOOK_ID_2);

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
        List<? extends Comment> comments = commService.findAllForBookFromReader(BOOK_ID_2, READER_ID_1);

        assertThat(comments.size()).isEqualTo(2);
        assertThat(comments)
                .allMatch(it -> it.getReader() != null)
                .allMatch(it -> it.getBook().getAuthor() != null)
                .allMatch(it -> !it.getBook().getGenres().isEmpty())
                .allMatch(it ->
                ((it.getBook().getId() == BOOK_ID_2) && (it.getReader().getId() == READER_ID_1)));
    }

    @DisplayName("Должен создавать новый комментарий")
    @DirtiesContext
    @Test
    void mustCreateComment() {
        Comment createdComm = commService.createComment(READER_ID_1, BOOK_ID_2, "Comment 100500");

        assertThat(createdComm.getId()).isGreaterThan(0);

        var found = commService.findById(createdComm.getId());

        assertThat(found).extracting(Comment::getId).isEqualTo(createdComm.getId());
        assertThat(found).extracting(Comment::getText).isEqualTo(createdComm.getText());
    }

    @DisplayName("Должен удалять все комментарии читателя")
    @DirtiesContext
    @Test
    void mustRemoveAllCommentsFromReader() {
        int nDel = commService.deleteAllFromReader(READER_ID_1);

        assertThat(nDel).isEqualTo(3);

        List<? extends Comment> comments = commService.findAll();

        assertThat(comments.size()).isEqualTo(5);
        assertThat(comments).allMatch(it -> it.getReader().getId() != READER_ID_1);
    }
}
