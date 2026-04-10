package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Author;
import ru.otus.hw.repositories.AuthorRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class AuthorServiceImpl implements AuthorService {
    private final AuthorRepository authorRepository;

    @Transactional(readOnly = true)
    @Override
    public List<Author> findAll() {
        return authorRepository.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public Author findById(long id) {
        Optional<Author> result = authorRepository.findById(id);
        if (result.isPresent()) {
            return result.get();
        } else {
            throw new EntityNotFoundException("Автоp для ID %d не найден".formatted(id));
        }
    }

    @Transactional(readOnly = true)
    @Override
    public Map<Long, Author> findByIds(Set<Long> ids) {
        return authorRepository.findByIdIn(ids).stream()
                .collect(Collectors.toMap(Author::getId, it -> it));
    }
}
