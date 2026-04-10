package ru.otus.hw.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.models.Reader;

import java.sql.Date;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе Spring ORM для работы с комментариями")
@DataJpaTest
public class CommentRepositoryTest {

    private static final long BOOK_ID_2 = 2L;
    private static final long READER_ID_1 = 1L;
    private static final long COMMENT_ID_1 = 1L;

    @Autowired
    TestEntityManager em;

    @Autowired
    private CommentRepository commentRepo;

    private List<Comment> dbComments;

    @BeforeEach
    void setUp() {
        dbComments = new ArrayList<>(10);
        for (long id = 1; id <= 8; id ++) {
            var comment = em.find(Comment.class, id);
            var txtComment = comment.toString();
            System.out.printf("\r\n---> Comment (%d): %s", id, txtComment);
            em.detach(comment);
            dbComments.add(comment);
        }
    }

    @DisplayName("Должен загружать комментарий по его ID")
    @Test
    void mustLoadCommentById() {
        for (Comment expectedComment : dbComments) {
            Optional<Comment> actualComment = commentRepo.findById(expectedComment.getId());

            assertThat(actualComment).isPresent().get().usingRecursiveComparison().isEqualTo(expectedComment);
        }
    }

    @DisplayName("Должен загружать все комментарии")
    @Test
    void mustLoadAllComments() {
        var expectedComments = dbComments;

        var actualComments = commentRepo.findAll();

        assertThat(actualComments)
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactlyElementsOf(expectedComments);
    }

    @DisplayName("Должен загружать все комментарии для книги")
    @Test
    void mustLoadAllCommentsForBook() {
        var expectedComments = dbComments.stream().filter(it -> it.getBook().getId() == BOOK_ID_2).toList();

        var actualComments = commentRepo.findByBook(Book.forId(BOOK_ID_2));

        assertThat(actualComments)
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactlyElementsOf(expectedComments);
    }

    @DisplayName("Должен загружать все комментарии от читателя")
    @Test
    void mustLoadAllCommentsFromReader() {
        var expectedComments = dbComments.stream()
                .filter(it -> it.getReader().getId() == READER_ID_1).toList();

        var actualComments = commentRepo.findByReader(Reader.forId(READER_ID_1));

        assertThat(actualComments)
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactlyElementsOf(expectedComments);
    }

    @DisplayName("Должен загружать все комментарии от читателя для определенной книги")
    @Test
    void mustLoadAllCommentsFromReaderForBook() {
        Book   book2   = em.find(Book.class,   BOOK_ID_2);
        Reader reader1 = em.find(Reader.class, READER_ID_1);

        var expectedComments = dbComments.stream()
                .filter(it -> (it.getReader().getId() == READER_ID_1) && (it.getBook().getId() == BOOK_ID_2))
                .toList();

        var actualComments = commentRepo.findByReaderAndBook(reader1, book2);

        assertThat(actualComments)
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactlyElementsOf(expectedComments);
    }

    @DisplayName("Должен создавать новый комментарий")
    @Test
    void mustCreateComment() {
        Calendar cal = new GregorianCalendar();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        Comment createdComm = commentRepo.saveAndFlush(
                Comment.builder()
                        .book(em.find(Book.class, BOOK_ID_2))
                        .reader(em.find(Reader.class, READER_ID_1))
                        .text("Comment 100500")
                        .date(new Date(cal.getTimeInMillis()))
                        .build()
        );

        assertThat(createdComm)
                .isNotNull()
                .matches(it -> it.getId() > 0, "ID созданного комментария почему-то 0");

        em.detach(createdComm);

        Comment foundComment = em.find(Comment.class, createdComm.getId());

        assertThat(foundComment).usingRecursiveComparison().isEqualTo(createdComm);
    }

    @DisplayName("Должен обновлять комментарий")
    @Test
    void mustUpdateComment() {
        Comment originalComment = em.find(Comment.class, COMMENT_ID_1);
        em.detach(originalComment);

        Calendar cal = new GregorianCalendar();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        Comment commentToUpdate = originalComment.toBuilder()
                .text("Comment 100500")
                .date(new Date(cal.getTimeInMillis()))
                .build();

        Comment updatedComment = commentRepo.saveAndFlush(commentToUpdate);

        Comment foundNewComment = em.find(Comment.class, COMMENT_ID_1);

        assertThat(updatedComment).usingRecursiveComparison().isEqualTo(commentToUpdate);
        assertThat(foundNewComment).usingRecursiveComparison().isEqualTo(commentToUpdate);
        assertThat(foundNewComment).usingRecursiveComparison().isNotEqualTo(originalComment);
    }

    @DisplayName("Должен удалять комментарий")
    @Test
    void mustDeleteComment() {
        Comment comment = em.find(Comment.class, COMMENT_ID_1);
        em.detach(comment);
        assertThat(comment).isNotNull();

        commentRepo.deleteById(COMMENT_ID_1);

        assertThat(em.find(Comment.class, COMMENT_ID_1)).isNull();
    }

    @DisplayName("Должен удалять комментарий")
    @Test
    void mustDeleteAllCommentsFromReader() {
        int nDeleted = commentRepo.deleteByReader(Reader.forId(READER_ID_1));

        assertThat(nDeleted).isEqualTo(3);

        List<Comment> allComments = new ArrayList<>(5);
        for (long id = 1; id <= 8; id ++) {
            var comment = em.find(Comment.class, id);
            if (comment != null) {
                allComments.add(comment);
            }
        }

        assertThat(allComments.size()).isEqualTo(5);

        var commentsForBook = allComments.stream().filter(it -> it.getReader().getId() == READER_ID_1).toList();
        assertThat(commentsForBook.size()).isEqualTo(0);
    }

}
