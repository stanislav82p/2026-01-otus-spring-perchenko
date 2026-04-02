package ru.otus.hw.repositories;

import ru.otus.hw.models.Reader;

import java.util.Optional;

/**
 * Этот репозиторий не реализован полностью. Это за пределами требований задачи
 * Реализован только метод извлечения читателя, который используется в сервисе
 * комментариев.
 */
public interface ReaderRepository {

    Optional<Reader> findById(long id);

}
