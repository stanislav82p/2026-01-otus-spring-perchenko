package ru.otus.hw.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.TestResult;
import ru.otus.hw.utils.QuestionTestDataProvider;

public class TestRunnerServiceImplTest {

    @Mock
    private StudentService studentService;

    @Mock
    private TestService testService;

    @Mock
    private ResultService resultService;

    private AutoCloseable mocks;

    @BeforeEach
    void setUp() {
        mocks = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception {
        mocks.close();
    }

    @DisplayName("Проверить сценарий выполнения тестирования")
    @Test
    void mustExecuteTestScenario() {
        var testStudent = new Student("Stanislav", "Perchenko");
        var testResult  = buildDemoTestResult(testStudent);

        BDDMockito.given(studentService.determineCurrentStudent()).willReturn(testStudent);
        BDDMockito.given(testService.executeTestFor(testStudent)).willReturn(testResult);

        TestRunnerService runnerService = new TestRunnerServiceImpl(testService, studentService, resultService);
        runnerService.run();

        BDDMockito.verify(studentService, Mockito.times(1)).determineCurrentStudent();
        BDDMockito.verify(testService, Mockito.times(1)).executeTestFor(testStudent);
        BDDMockito.verify(resultService, Mockito.times(1)).showResult(testResult);
    }

    private TestResult buildDemoTestResult(Student stud) {
        var questions = QuestionTestDataProvider.buildExpectedQuestions();
        var result = new TestResult(new Student("Stanislav", "Perchenko"));
        result.applyAnswer(questions.get(0), false);
        result.applyAnswer(questions.get(1), true);
        return result;
    }
}
