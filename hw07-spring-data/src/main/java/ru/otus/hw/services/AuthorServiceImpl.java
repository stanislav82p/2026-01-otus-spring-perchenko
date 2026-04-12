package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.dto.AuthorDto;
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
    public List<AuthorDto> findAll() {
        return authorRepository.findAll().stream().map(AuthorDto::fromEntity).toList();
    }

    @Transactional(readOnly = true)
    @Override
    public AuthorDto findById(long id) {
        Optional<Author> result = authorRepository.findById(id);
        if (result.isPresent()) {
            return AuthorDto.fromEntity(result.get());
        } else {
            throw new EntityNotFoundException("Автоp для ID %d не найден".formatted(id));
        }
    }

    @Transactional(readOnly = true)
    @Override
    public Map<Long, AuthorDto> findByIds(Set<Long> ids) {
        return authorRepository.findByIdIn(ids).stream()
                .collect(Collectors.toMap(Author::getId, AuthorDto::fromEntity));
    }
}
