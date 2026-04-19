package ru.otus.hw.services;

import ru.otus.hw.models.dto.AuthorDto;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface AuthorService {
    List<AuthorDto> findAll();

    AuthorDto findById(long id);

    Map<Long, AuthorDto> findByIds(Set<Long> ids);
}
