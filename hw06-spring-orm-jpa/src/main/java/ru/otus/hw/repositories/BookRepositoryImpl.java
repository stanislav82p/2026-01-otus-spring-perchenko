package ru.otus.hw.repositories;


import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.Book;

import java.util.List;
import java.util.Optional;

import static org.springframework.data.jpa.repository.EntityGraph.EntityGraphType.FETCH;

@Repository
public class BookRepositoryImpl implements BookRepository {

    @PersistenceContext
    private final EntityManager em;

    public BookRepositoryImpl(EntityManager em) {
        this.em = em;
    }

    @Override
    public List<Book> findAll() {
        TypedQuery<Book> query = em.createQuery("SELECT b FROM Book b", Book.class);
        query.setHint(FETCH.getKey(), em.getEntityGraph("book-author-entity-graph"));
        return query.getResultList();
    }

    @Override
    public Optional<Book> findById(long id) {
        var jpql = "SELECT b FROM Book b  LEFT JOIN FETCH b.genres WHERE b.id = :book_id";
        TypedQuery<Book> query = em.createQuery(jpql, Book.class);
        query.setParameter("book_id", id);
        query.setHint(FETCH.getKey(), em.getEntityGraph("book-author-entity-graph"));
        List<Book> result = query.getResultList();
        return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
    }

    @Override
    public Book save(Book book) {
        if (book.getId() == 0) {
            em.persist(book);
            return book;
        } else {
            return em.merge(book);
        }
    }

    @Override
    public int deleteById(long id) {
        // У нас в книге нету каментов, поэтому удаляем вручную
        Query query = em.createQuery("DELETE FROM Comment c WHERE c.book.id = :id");
        query.setParameter("id", id);
        query.executeUpdate();

        query = em.createQuery("DELETE FROM Book b WHERE b.id = :id");
        query.setParameter("id", id);
        return query.executeUpdate();
    }
}
