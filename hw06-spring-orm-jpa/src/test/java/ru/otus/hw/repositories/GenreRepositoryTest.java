package ru.otus.hw.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.hw.models.Genre;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе Spring ORM для работы с жанрами")
@DataJpaTest
@Import({GenreRepositoryImpl.class})
public class GenreRepositoryTest {

    @Autowired
    private TestEntityManager em;

    @Autowired
    private GenreRepository genreRepo;

    private List<Genre> dbGenres;

    @BeforeEach
    void setUp() {
        dbGenres = new ArrayList<>(6);
        for (long id = 1; id <= 6; id ++) {
            var genre = em.find(Genre.class, id);
            em.detach(genre);
            dbGenres.add(genre);
        }
    }

    @DisplayName("Должен загружать список всех жанров")
    @Test
    void shouldReturnCorrectGenreList() {
        List<Genre> actualGenres = genreRepo.findAll();

        assertThat(actualGenres)
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactlyElementsOf(dbGenres);
    }

    @DisplayName("Должен загружать жанр по id")
    @Test
    void shouldReturnCorrectGenresByIds() {
        Set<Genre> expectedGenres = Set.of(dbGenres.get(1), dbGenres.get(3), dbGenres.get(4));

        Set<Long> ids = expectedGenres.stream().map(Genre::getId).collect(Collectors.toSet());
        Set<Genre> actualGenres = genreRepo.findAllByIds(ids);

        assertThat(actualGenres)
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactlyInAnyOrderElementsOf(expectedGenres);
    }

    @DisplayName("Не должен загружать жанр по неверному id")
    @Test
    void shouldNotReturnAuthorForWrongId() {
        Optional<Genre> actualGenre = genreRepo.findById(100500);
        assertThat(actualGenre).isNotPresent();
    }

}
