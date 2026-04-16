package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Genre;
import ru.otus.hw.models.entity.GenreEntity;
import ru.otus.hw.models.dto.GenreDto;
import ru.otus.hw.repositories.GenreRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class GenreServiceImpl implements GenreService {
    private final GenreRepository genreRepository;

    @Transactional(readOnly = true)
    @Override
    public List<GenreDto> findAll() {
        return genreRepository.findAll().stream().map(GenreDto::fromGenre).toList();
    }

    @Transactional(readOnly = true)
    @Override
    public GenreDto findById(long id) {
        Optional<GenreEntity> result = genreRepository.findById(id);
        if (result.isPresent()) {
            return GenreDto.fromGenre(result.get());
        } else {
            throw new EntityNotFoundException("Жанр для ID %d не найден".formatted(id));
        }
    }

    @Transactional(readOnly = true)
    @Override
    public Map<Long, Genre> findByIds(Set<Long> ids) {
        return genreRepository.findByIdIn(ids).stream()
                .collect(Collectors.toMap(GenreEntity::getId, GenreDto::fromGenre));
    }
}
