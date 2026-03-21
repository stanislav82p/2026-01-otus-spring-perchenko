package ru.otus.hw.shell;

import org.assertj.core.api.Assertions;
import org.jline.terminal.Terminal;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.shell.InputProvider;
import org.springframework.shell.Shell;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import ru.otus.hw.service.StudentService;
import ru.otus.hw.service.TestRunnerService;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@DisplayName("Тест команд shell: AppCommands")
@SpringBootTest
public class AppCommandsTest {

    @Mock
    private InputProvider inputProvider;

    private ArgumentCaptor<String> argument1Captor, argument2Captor;

    @MockitoSpyBean
    private StudentService studentService;

    @MockitoSpyBean
    private Terminal mockedTerminal;

    @MockitoBean
    private TestRunnerService testRunner;

    @Autowired
    private Shell shell;

    private AutoCloseable mocks;

    @BeforeEach
    void setUp() {
        mocks = MockitoAnnotations.openMocks(this);
        argument1Captor = ArgumentCaptor.forClass(String.class);
        argument2Captor = ArgumentCaptor.forClass(String.class);
    }

    @AfterEach
    void tearDown() throws Exception {
        mocks.close();
    }

    @DisplayName("Проверка команды 'login'")
    @Test
    void testLogin() throws Exception {
        given(inputProvider.readInput())
                .willReturn(() -> "login --first-name Stanislav --last-name Perchenko")
                .willReturn(() -> "login -f Stanislav -l Perchenko")
                .willReturn(null);

        // Мокаем терминал, чтобы смотреть вывод shell-a
        var os = new ByteArrayOutputStream();
        given(mockedTerminal.writer()).willReturn(new PrintWriter(os));

        shell.run(inputProvider);

        verify(studentService, times(2)).login(argument1Captor.capture(), argument2Captor.capture());

        List<String> firstNames = argument1Captor.getAllValues();
        List<String> lastNames = argument2Captor.getAllValues();

        Assertions.assertThat(firstNames).containsExactlyInAnyOrder("Stanislav", "Stanislav");
        Assertions.assertThat(lastNames).containsExactlyInAnyOrder("Perchenko", "Perchenko");

       Assertions.assertThat(new String(os.toByteArray()))
               .isEqualTo("Welcome, Stanislav Perchenko\r\nWelcome, Stanislav Perchenko\r\n");
    }

    @DisplayName("Проверка команды 'logout' когда не залогинены")
    @Test
    void testLogoutWhenNotLoggedIn() throws Exception {
        given(inputProvider.readInput())
                .willReturn(() -> "logout")
                .willReturn(null);

        // Мокаем терминал, чтобы смотреть вывод shell-a
        var os = new ByteArrayOutputStream();
        given(mockedTerminal.writer()).willReturn(new PrintWriter(os));

        shell.run(inputProvider);

        verify(studentService, times(1)).logout();

        Assertions.assertThat(new String(os.toByteArray())).isEqualTo("\r\n");
    }

    @DisplayName("Проверка команды 'logout' когда залогинены")
    @Test
    void testLogoutWhenLoggedIn() throws Exception {
        given(inputProvider.readInput())
                .willReturn(() -> "login --first-name Stanislav --last-name Perchenko")
                .willReturn(() -> "logout")
                .willReturn(null);

        // Мокаем терминал, чтобы смотреть вывод shell-a
        var os = new ByteArrayOutputStream();
        given(mockedTerminal.writer()).willReturn(new PrintWriter(os));

        shell.run(inputProvider);

        verify(studentService, times(1)).login(any(), any());
        verify(studentService, times(1)).logout();

        Assertions.assertThat(new String(os.toByteArray()))
                .isEqualTo("Welcome, Stanislav Perchenko\r\nGoodbye, Stanislav Perchenko\r\n");
    }

    @DisplayName("Должен запускать тестирование когда залогинен")
    @Test
    void mustRunTestWhenLoggedIn() throws Exception {
        given(inputProvider.readInput())
                .willReturn(() -> "login --first-name Stanislav --last-name Perchenko")
                .willReturn(() -> "test")
                .willReturn(null);

        shell.run(inputProvider);

        verify(studentService, times(1)).login(any(), any());
        verify(testRunner, times(1)).run();
    }

    @DisplayName("Должен запускать тестирование когда НЕ залогинен")
    @Test
    void mustRunTestWhenNotLoggedIn() throws Exception {
        given(inputProvider.readInput())
                .willReturn(() -> "test")
                .willReturn(null);

        shell.run(inputProvider);

        verify(testRunner, times(1)).run();
    }
}
