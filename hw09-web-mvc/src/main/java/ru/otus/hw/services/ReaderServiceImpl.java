package ru.otus.hw.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.otus.hw.models.Reader;
import ru.otus.hw.models.dto.ReaderDto;
import ru.otus.hw.repositories.ReaderRepository;

import java.util.List;

@Service
public class ReaderServiceImpl implements ReaderService {

    private final ReaderRepository readerRepository;

    @Autowired
    public ReaderServiceImpl(ReaderRepository readerRepository) {
        this.readerRepository = readerRepository;
    }

    @Override
    public List<? extends Reader> findAll() {
        return readerRepository.findAll().stream().map(ReaderDto::fromEntity).toList();
    }
}
