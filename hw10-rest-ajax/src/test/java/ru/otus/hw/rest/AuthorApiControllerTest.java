package ru.otus.hw.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.otus.hw.models.dto.AuthorDto;
import ru.otus.hw.models.entity.AuthorEntity;
import ru.otus.hw.services.AuthorService;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("API-контроллер для авторов")
@WebMvcTest(AuthorApiController.class)
public class AuthorApiControllerTest {

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

        mvc.perform(MockMvcRequestBuilders.get("/api/library/authors"))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedJson));
    }
}
