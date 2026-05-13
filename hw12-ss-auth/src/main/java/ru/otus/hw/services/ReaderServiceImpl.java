package ru.otus.hw.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.dto.ReaderDto;
import ru.otus.hw.models.entity.ReaderEntity;
import ru.otus.hw.repositories.ReaderRepository;
import ru.otus.hw.rest.response.ProfileResponseDto;
import ru.otus.hw.services.localization.LocalizedMessagesService;

import java.util.List;
import java.util.Optional;

@Service
public class ReaderServiceImpl implements ReaderService {

    private final ReaderRepository readerRepository;

    private final LocalizedMessagesService messageService;

    @Autowired
    public ReaderServiceImpl(ReaderRepository readerRepository, LocalizedMessagesService messageService) {
        this.readerRepository = readerRepository;
        this.messageService = messageService;
    }

    @Override
    public List<ReaderDto> findAll() {
        return readerRepository.findAll().stream().map(ReaderDto::fromEntity).toList();
    }

    @Override
    public ProfileResponseDto getByUsername(String username) {
        Optional<ReaderEntity> optReader = readerRepository.findById(username);
        if (optReader.isEmpty()) {
            throw new EntityNotFoundException(
                    "The reader with ID %s was not found".formatted(username),
                    messageService.getMessage("reader-not-found")
            );
        }
        return new ProfileResponseDto(optReader.get());
    }
}
