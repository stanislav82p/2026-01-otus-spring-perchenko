package ru.otus.hw.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.TestResult;
import ru.otus.hw.exceptions.NotLoggedInException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@DisplayName("Тест сервиса запуска тестирования: TestRunnerServiceImpl")
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class TestRunnerServiceImplTest {

    @MockitoBean
    private StudentService mockedStudentService;

    @MockitoBean
    private TestService mockedTestService;

    @MockitoBean
    private ResultService mockedResultService;

    @MockitoSpyBean
    private TestRunnerService testRunnerService;

    @DisplayName("Должен выполнять тестирование когда залогинен")
    @Test
    void mustExecuteTestScenarioWhenLoggedIn() {
        var student = new Student("Stanislav", "Perchenko");
        var result  = new TestResult(student);
        given(mockedStudentService.isLoggedIn()).willReturn(true);
        given(mockedStudentService.getCurrentStudent()).willReturn(student);
        given(mockedTestService.executeTestFor(any())).willReturn(result);

        testRunnerService.run();

        verify(mockedStudentService, times(1)).isLoggedIn();
        verify(mockedStudentService, times(1)).getCurrentStudent();
        verify(mockedTestService, times(1)).executeTestFor(student);
        verify(mockedResultService, times(1)).showResult(result);
    }

    @DisplayName("Должен выбрасывать исключение когда НЕ залогинен")
    @Test
    void mustThrowExceptionWhenNotLoggedIn() {
        given(mockedStudentService.isLoggedIn()).willReturn(false);

        var exception = assertThrows(
                NotLoggedInException.class,
                testRunnerService::run
        );

        Assertions.assertThat(exception.getMessage()).isNull();
    }
}
