package ru.otus.hw.dao;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.dao.convert.QuestionEntityConverter;
import ru.otus.hw.exceptions.QuestionReadException;
import ru.otus.hw.utils.QuestionTestDataProvider;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

public class CsvQuestionDaoTest {

    @Mock
    private TestFileNameProvider fileNameProvider;

    private AutoCloseable mocks;

    @BeforeEach
    void setUp() {
        mocks = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception {
        mocks.close();
    }

    @DisplayName("должен выбрасить исключение, если CSV файл отсутствует")
    @Test
    void mustThrowExceptionOnNoFile() {
        BDDMockito.given(fileNameProvider.testFileName()).willReturn("vali-d.csv");

        QuestionDao dao = new CsvQuestionDao(fileNameProvider, new QuestionEntityConverter());

        var exception = assertThrows(
                QuestionReadException.class,
                dao::findAll
        );

        assertEquals("questions resource file not available", exception.getMessage());
    }

    @DisplayName("должен выбрасить исключение, если CSV файл содержит невалидные данные")
    @Test
    void mustThrowExceptionOnInvalidData() {
        BDDMockito.given(fileNameProvider.testFileName()).willReturn("invalid.csv");

        QuestionDao dao = new CsvQuestionDao(fileNameProvider, new QuestionEntityConverter());

        var exception = assertThrows(
                RuntimeException.class,
                dao::findAll
        );

        var cause = exception.getCause();
        assertInstanceOf(IllegalArgumentException.class, cause);
        assertEquals("The provided value is not Boolean: troe", cause.getMessage());
    }

    @DisplayName("должен правильно прочитать вопросы из CSV файла")
    @Test
    void readValidCsvResources() {
        BDDMockito.given(fileNameProvider.testFileName()).willReturn("valid.csv");

        QuestionDao dao = new CsvQuestionDao(fileNameProvider, new QuestionEntityConverter());

        var realQuestions = dao.findAll();
        var expectedQuestions = QuestionTestDataProvider.buildExpectedQuestions();

        assertEquals(expectedQuestions, realQuestions);
    }

}
