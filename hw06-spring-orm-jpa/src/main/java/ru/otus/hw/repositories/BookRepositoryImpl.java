package ru.otus.hw.repositories;


import org.springframework.stereotype.Repository;
import ru.otus.hw.models.Book;

import java.util.List;
import java.util.Optional;

@Repository
public class BookRepositoryImpl implements BookRepository{
    @Override
    public Optional<Book> findById(long id) {
        return Optional.empty();
    }

    @Override
    public List<Book> findAll() {
        return List.of();
    }

    @Override
    public Book save(Book book) {
        return null;
    }

    @Override
    public void deleteById(long id) {

    }
}
