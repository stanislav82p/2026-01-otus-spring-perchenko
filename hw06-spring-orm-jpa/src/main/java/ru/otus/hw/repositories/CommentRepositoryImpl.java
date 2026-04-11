package ru.otus.hw.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.models.Reader;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Optional;

import static org.springframework.data.jpa.repository.EntityGraph.EntityGraphType.FETCH;

@Repository
public class CommentRepositoryImpl implements CommentRepository {

    @PersistenceContext
    private final EntityManager em;

    public CommentRepositoryImpl(EntityManager em) {
        this.em = em;
    }

    @Override
    public List<Comment> findAll() {
        TypedQuery<Comment> query = em.createQuery("SELECT c FROM Comment c", Comment.class);
        query.setHint(FETCH.getKey(), em.getEntityGraph("comment-all-depends-entity-graph"));
        List<Comment> comments = query.getResultList();

        List<Author> authors = selectAuthorsForBooksOfComments(comments);

        return comments;
    }

    @Override
    public Optional<Comment> findById(long id) {
        TypedQuery<Comment> query = em.createQuery("SELECT c FROM Comment c WHERE c.id = :comment_id", Comment.class);
        query.setParameter("comment_id", id);
        query.setHint(FETCH.getKey(), em.getEntityGraph("comment-all-depends-entity-graph"));
        List<Comment> result = query.getResultList();
        return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
    }

    @Override
    public List<Comment> findAllForBook(long bookId) {
        // Тут книгу не джойним, поскольку она у нас одна. Сходим за ней отдельно.
        TypedQuery<Comment> query = em.createQuery("SELECT c FROM Comment c WHERE c.book.id = :book_id", Comment.class);
        query.setParameter("book_id", bookId);
        query.setHint(FETCH.getKey(), em.getEntityGraph("comment-reader-entity-graph"));
        return query.getResultList();
    }

    @Override
    public List<Comment> findAllFromReader(long readerId) {
        // А тут читателя не джойним, поскольку он у нас один. Сходим за ним отдельно.
        TypedQuery<Comment> query = em.createQuery(
                "SELECT c FROM Comment c WHERE c.reader.id = :reader_id", Comment.class);
        query.setParameter("reader_id", readerId);
        query.setHint(FETCH.getKey(), em.getEntityGraph("comment-book-entity-graph"));
        List<Comment> comments = query.getResultList();

        List<Author> authors = selectAuthorsForBooksOfComments(comments);

        return comments;
    }

    @Override
    public List<Comment> findAllFromReaderForBook(Reader reader, Book book) {
        TypedQuery<Comment> query = em.createQuery(
                "SELECT c FROM Comment c LEFT JOIN FETCH c.book LEFT JOIN FETCH c.reader " +
                        "WHERE c.book.id = :book_id AND c.reader.id = :reader_id", Comment.class);
        query.setParameter("reader_id", reader.getId());
        query.setParameter("book_id", book.getId());
        return query.getResultList();
    }

    @Override
    public Comment createComment(Book book, Reader reader, String text) {
        Calendar cal = new GregorianCalendar();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        var comment = new Comment(0, book, reader, text, new Date(cal.getTimeInMillis()));
        em.persist(comment);
        // ID этого объекта заполняется валидным значением
        return comment;
    }

    @Override
    public int deleteById(long commentId) {
        Query query = em.createQuery("DELETE FROM Comment c WHERE c.id = :id");
        query.setParameter("id", commentId);
        return query.executeUpdate();
    }

    @Override
    public int deleteAllFromReader(long readerId) {
        Query query = em.createQuery("DELETE FROM Comment c WHERE c.reader.id = :reader_id");
        query.setParameter("reader_id", readerId);
        return query.executeUpdate();
    }

    @Override
    public boolean update(long commentId, String text) {
        Query query = em.createQuery("UPDATE Comment c SET c.text = :new_text, c.date = :new_date WHERE c.id = :id");
        query.setParameter("id", commentId);
        query.setParameter("new_text", text);
        query.setParameter("new_date", new Date(System.currentTimeMillis()));
        var nUpd = query.executeUpdate();
        return nUpd == 1;
    }

    private List<Author> selectAuthorsForBooksOfComments(List<Comment> comments) {
        List<Long> authorIds = comments.stream()
                .map(it -> it.getBook().getAuthor().getId())
                .distinct()
                .toList();
        List<Author> authors = new ArrayList<>(10);
        // Выбираем в контекст авторов для книг подзапросами по 10 штук
        while (!authorIds.isEmpty()) {
            int n = Math.min(authorIds.size(), 10);
            List<Long> ids = authorIds.subList(0, n);
            if (authorIds.size() > n) {
                authorIds = authorIds.subList(n, authorIds.size());
            } else {
                authorIds = List.of();
            }

            TypedQuery<Author> aQuery =
                    em.createQuery("SELECT a FROM Author a WHERE a.id IN (:author_ids)", Author.class);
            aQuery.setParameter("author_ids", ids);
            authors.addAll(aQuery.getResultList());
        }
        return authors;
    }
}
