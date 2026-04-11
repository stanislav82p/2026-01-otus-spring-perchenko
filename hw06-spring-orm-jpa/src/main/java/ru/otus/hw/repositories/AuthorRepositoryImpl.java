package ru.otus.hw.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.Author;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
public class AuthorRepositoryImpl implements AuthorRepository {

    @PersistenceContext
    private final EntityManager em;

    @Autowired
    public AuthorRepositoryImpl(EntityManager em) {
        this.em = em;
    }

    @Override
    public List<Author> findAll() {
        TypedQuery<Author> query = em.createQuery("SELECT a FROM Author a", Author.class);
        return query.getResultList();
    }

    @Override
    public Optional<Author> findById(long id) {
        return Optional.ofNullable(em.find(Author.class, id));
    }

    @Override
    public Map<Long, Author> findByIds(Set<Long> ids) {
        TypedQuery<Author> query = em.createQuery("SELECT a FROM Author a WHERE a.id IN :ids", Author.class);
        query.setParameter("ids", ids);
        return query.getResultList().stream().collect(Collectors.toMap(Author::getId, a -> a));
    }
}
