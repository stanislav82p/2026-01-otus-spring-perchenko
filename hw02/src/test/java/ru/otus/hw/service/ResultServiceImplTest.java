package ru.otus.hw.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.otus.hw.config.TestConfig;
import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.TestResult;
import ru.otus.hw.service.io.StreamsIOService;
import ru.otus.hw.utils.QuestionTestDataProvider;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ResultServiceImplTest {

    @Mock
    private TestConfig testConfig;

    private AutoCloseable mocks;

    @BeforeEach
    void setUp() {
        mocks = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception {
        mocks.close();
    }

    @DisplayName("Проверка вывода для успешного теста")
    @Test
    void mustPrintCorrectForPassedTest() {
        BDDMockito.given(testConfig.getRightAnswersCountToPass()).willReturn(1);

        var sink = new ByteArrayOutputStream();

        ResultService rService = new ResultServiceImpl(testConfig, new StreamsIOService(new PrintStream(sink), System.in));

        var questions = QuestionTestDataProvider.buildExpectedQuestions();
        var result = new TestResult(new Student("Stanislav", "Perchenko"));
        result.applyAnswer(questions.get(0), false);
        result.applyAnswer(questions.get(1), true);

        rService.showResult(result);
        final String printText = new String(sink.toByteArray());

        final String refOutput = """
                \r
                Test results: \r
                Student: Stanislav Perchenko\r
                Answered questions count: 2\r
                Correct answers count: 1\r
                Congratulations! You passed test!\r
                """;

        assertEquals(refOutput, printText);
    }

    @DisplayName("Проверка вывода для проваленного теста")
    @Test
    void mustPrintCorrectForFailedTest() {
        BDDMockito.given(testConfig.getRightAnswersCountToPass()).willReturn(2);

        var sink = new ByteArrayOutputStream();

        ResultService rService = new ResultServiceImpl(testConfig, new StreamsIOService(new PrintStream(sink), System.in));

        var questions = QuestionTestDataProvider.buildExpectedQuestions();
        var result = new TestResult(new Student("Stanislav", "Perchenko"));
        result.applyAnswer(questions.get(0), false);
        result.applyAnswer(questions.get(1), true);

        rService.showResult(result);
        final String printText = new String(sink.toByteArray());

        final String refOutput = """
                \r
                Test results: \r
                Student: Stanislav Perchenko\r
                Answered questions count: 2\r
                Correct answers count: 1\r
                Sorry, you failed the test. Minimum correct answers to pass is 2\r
                """;

        assertEquals(refOutput, printText);
    }
}
