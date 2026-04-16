package ru.otus.hw.services;

import ru.otus.hw.models.Reader;

import java.util.List;

public interface ReaderService {
    List<? extends Reader> findAll();
}
