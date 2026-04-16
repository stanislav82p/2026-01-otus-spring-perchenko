package ru.otus.hw.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.Reader;

import java.util.Optional;

@Repository
public class ReaderRepositoryImpl implements ReaderRepository {

    @PersistenceContext
    private final EntityManager em;

    @Autowired
    public ReaderRepositoryImpl(EntityManager em) {
        this.em = em;
    }

    @Override
    public Optional<Reader> findById(long id) {
        return Optional.ofNullable(em.find(Reader.class, id));
    }
}
