package ru.otus.hw.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.otus.hw.models.entity.AuthorEntity;

import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе Spring ORM для работы с авторами")
@DataJpaTest
public class AuthorRepositoryTest {

    public static final long AUTHOR_ID_1 = 1L;
    public static final long AUTHOR_ID_3 = 3L;

    @Autowired
    private TestEntityManager em;

    @Autowired
    private AuthorRepository authorRepo;

    private List<AuthorEntity> dbAuthors;

    @BeforeEach
    void setUp() {
        dbAuthors = new ArrayList<>(4);
        for (long id = 1; id <= 4; id ++) {
            var author = em.find(AuthorEntity.class, id);
            em.detach(author);
            dbAuthors.add(author);
        }
    }

    @DisplayName("Должен загружать список всех авторов")
    @Test
    void shouldReturnCorrectAuthorList() {
        List<AuthorEntity> actualAuthors = authorRepo.findAll();

        assertThat(actualAuthors)
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactlyElementsOf(dbAuthors);
    }

    @DisplayName("Должен загружать автора по id")
    @Test
    void shouldReturnCorrectAuthorById() {
        for (AuthorEntity expectedAuthor : dbAuthors) {
            Optional<AuthorEntity> optAuthor = authorRepo.findById(expectedAuthor.getId());

            assertThat(optAuthor)
                    .isPresent()
                    .get()
                    .usingRecursiveComparison()
                    .isEqualTo(expectedAuthor);
        }
    }

    @DisplayName("Должен вернуть нескольких авторов по заданным id")
    @Test
    void shouldReturnExpectedAuthors() {
        AuthorEntity expected1 = em.find(AuthorEntity.class, AUTHOR_ID_1);
        AuthorEntity expected3 = em.find(AuthorEntity.class, AUTHOR_ID_3);
        em.detach(expected1);
        em.detach(expected3);

        Set<Long> expectedIds = Set.of(1L, 3L);
        Map<Long, AuthorEntity> actualAuthors = authorRepo.findByIdIn(expectedIds).stream()
                .collect(Collectors.toMap(AuthorEntity::getId, it -> it));

        assertThat(actualAuthors.get(AUTHOR_ID_1)).isNotNull().usingRecursiveComparison().isEqualTo(expected1);
        assertThat(actualAuthors.get(AUTHOR_ID_3)).isNotNull().usingRecursiveComparison().isEqualTo(expected3);
    }

    @DisplayName("Не должен загружать автора по неверному id")
    @Test
    void shouldNotReturnAuthorForWrongId() {
        Optional<AuthorEntity> actualAuthor = authorRepo.findById(100500L);
        assertThat(actualAuthor).isNotPresent();
    }
}
