package ru.otus.hw.pages;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.otus.hw.sequrity.AppSecurityConfiguration;
import ru.otus.hw.sequrity.UserRole;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RootController.class)
@Import({AppSecurityConfiguration.class})
public class RootControllerTest {

    @Autowired
    private MockMvc mvc;

    @Test
    void testRedirectFromRoot() throws Exception {
        mvc.perform(
                        MockMvcRequestBuilders.get("/").with(
                                user("usr1").authorities(UserRole.MODERATOR.getGrantedAuthorities())
                        )
                ).andExpect(MockMvcResultMatchers.view().name("redirect:/books"));
    }

    @DisplayName("Должен возвращать 302 без авторизации и редирект на логин")
    @Test
    void shouldRedirectWhenNoAuth() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/"))
                .andExpect(status().isFound())
                .andExpect(header().string("Location", "http://localhost/login"));
    }
}
