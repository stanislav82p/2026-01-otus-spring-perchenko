package ru.otus.hw.services;

import ru.otus.hw.models.dto.ReaderDto;

import java.util.List;

public interface ReaderService {
    List<ReaderDto> findAll();
}
