package ru.otus.hw.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.otus.hw.models.dto.AuthorDto;
import ru.otus.hw.models.entity.AuthorEntity;
import ru.otus.hw.sequrity.AppSecurityConfiguration;
import ru.otus.hw.sequrity.UserRole;
import ru.otus.hw.services.AuthorService;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.mockito.ArgumentMatchers.eq;

@DisplayName("API-контроллер для авторов")
@WebMvcTest(AuthorApiController.class)
@Import({AppSecurityConfiguration.class})
public class AuthorApiControllerTest {

    public static final long AUTHOR_ID_1 = 1L;
    public static final long AUTHOR_ID_2 = 2L;
    public static final long AUTHOR_ID_3 = 3L;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockitoBean
    private AuthorService authorService;

    private List<AuthorDto> mAuthors;

    @BeforeEach
    void setUp() {
        mAuthors = new ArrayList<>(6);
        for (long i = 1; i < 6; i ++) {
            var a = new AuthorEntity(i, "Author_" + i);
            mAuthors.add(AuthorDto.fromEntity(a));
        }
    }

    @DisplayName("Должен возвращать всех авторов")
    @Test
    void shouldReturnCorrectAuthorList() throws Exception {
        given(authorService.findAll()).willReturn(mAuthors);

        String expectedJson = mapper.writeValueAsString(mAuthors);

        mvc.perform(MockMvcRequestBuilders.get("/api/library/authors").with(
                        user("usr1").authorities(UserRole.MODERATOR.getGrantedAuthorities())
                ))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedJson));
    }

    @DisplayName("Должен возвращать корректного автора по ID")
    @Test
    void shouldReturnAuthorById() throws Exception {
        var expectedAuthor = mAuthors.stream().filter(it -> it.getId() == AUTHOR_ID_1).findFirst().get();
        given(authorService.findById(eq(AUTHOR_ID_1))).willReturn(expectedAuthor);

        String expectedJson = mapper.writeValueAsString(expectedAuthor);

        mvc.perform(MockMvcRequestBuilders.get("/api/library/authors/"+AUTHOR_ID_1).with(
                        user("usr1").authorities(UserRole.MODERATOR.getGrantedAuthorities())
                ))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedJson));
    }

    @DisplayName("Должен возвращать 302 без авторизации и редирект на логин")
    @Test
    void shouldRedirectWhenNoAuth() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/api/library/authors"))
                .andExpect(status().isFound())
                .andExpect(header().string("Location", "http://localhost/login"));

        mvc.perform(MockMvcRequestBuilders.get("/api/library/authors/"+AUTHOR_ID_1))
                .andExpect(status().isFound())
                .andExpect(header().string("Location", "http://localhost/login"));
    }



}
