package ru.otus.hw.services;

import ru.otus.hw.models.dto.ReaderDto;
import ru.otus.hw.rest.response.ProfileResponseDto;

import java.util.List;

public interface ReaderService {

    List<ReaderDto> findAll();

    ProfileResponseDto getByUsername(String username);
}
