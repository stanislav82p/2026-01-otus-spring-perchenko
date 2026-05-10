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
import ru.otus.hw.models.dto.GenreDto;
import ru.otus.hw.models.entity.GenreEntity;
import ru.otus.hw.services.GenreService;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("API-контроллер для жанров")
@WebMvcTest(GenreApiController.class)
public class GenreApiControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockitoBean
    private GenreService genreService;

    private List<GenreDto> mGenres;

    @BeforeEach
    void setUp() {
        mGenres = new ArrayList<>(6);
        for (long i = 1; i < 6; i ++) {
            var genre = new GenreEntity(i, "Genre_" + i);
            mGenres.add(GenreDto.fromGenre(genre));
        }
    }

    @DisplayName("Должен возвращать все жанры")
    @Test
    void shouldReturnCorrectAuthorList() throws Exception {
        given(genreService.findAll()).willReturn(mGenres);

        String expectedJson = mapper.writeValueAsString(mGenres);

        mvc.perform(MockMvcRequestBuilders.get("/api/library/genres"))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedJson));
    }
}
