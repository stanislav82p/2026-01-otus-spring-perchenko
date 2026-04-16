package ru.otus.hw.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.hw.models.Author;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе Spring ORM для работы с авторами")
@DataJpaTest
@Import({AuthorRepositoryImpl.class})
public class AuthorRepositoryTest {

    public static final long AUTHOR_ID_1 = 1L;
    public static final long AUTHOR_ID_3 = 3L;

    @Autowired
    private TestEntityManager em;

    @Autowired
    private AuthorRepository authorRepo;

    private List<Author> dbAuthors;

    @BeforeEach
    void setUp() {
        dbAuthors = new ArrayList<>(4);
        for (long id = 1; id <= 4; id ++) {
            var author = em.find(Author.class, id);
            em.detach(author);
            dbAuthors.add(author);
        }
    }

    @DisplayName("Должен загружать список всех авторов")
    @Test
    void shouldReturnCorrectAuthorList() {
        List<Author> actualAuthors = authorRepo.findAll();

        assertThat(actualAuthors)
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactlyElementsOf(dbAuthors);
    }

    @DisplayName("Должен загружать автора по id")
    @Test
    void shouldReturnCorrectAuthorById() {
        for (Author expectedAuthor : dbAuthors) {
            Optional<Author> optAuthor = authorRepo.findById(expectedAuthor.getId());

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
        Author expected1 = em.find(Author.class, AUTHOR_ID_1);
        Author expected3 = em.find(Author.class, AUTHOR_ID_3);
        em.detach(expected1);
        em.detach(expected3);

        Set<Long> expectedIds = Set.of(1L, 3L);
        Map<Long, Author> actualAuthors = authorRepo.findByIds(expectedIds);

        assertThat(actualAuthors.get(AUTHOR_ID_1)).isNotNull().usingRecursiveComparison().isEqualTo(expected1);
        assertThat(actualAuthors.get(AUTHOR_ID_3)).isNotNull().usingRecursiveComparison().isEqualTo(expected3);
    }

    @DisplayName("Не должен загружать автора по неверному id")
    @Test
    void shouldNotReturnAuthorForWrongId() {
        Optional<Author> actualAuthor = authorRepo.findById(100500);
        assertThat(actualAuthor).isNotPresent();
    }
}
