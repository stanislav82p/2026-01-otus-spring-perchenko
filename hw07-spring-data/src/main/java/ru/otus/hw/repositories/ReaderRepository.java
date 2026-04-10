package ru.otus.hw.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.hw.models.Reader;

/**
 * Этот репозиторий не реализован полностью. Это за пределами требований задачи
 * Реализован только метод извлечения читателя, который используется в сервисе
 * комментариев.
 */
public interface ReaderRepository extends JpaRepository<Reader, Long> {

}
