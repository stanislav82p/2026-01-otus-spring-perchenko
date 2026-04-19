package ru.otus.hw.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static ru.otus.hw.controller.RootController.BASE_URL;

@WebMvcTest(RootController.class)
public class RootControllerTest {

    @Autowired
    private MockMvc mvc;

    @Test
    void testRedirectFromRoot() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/"))
                .andExpect(MockMvcResultMatchers.view().name("redirect:%s/books".formatted(BASE_URL)));
    }
}
