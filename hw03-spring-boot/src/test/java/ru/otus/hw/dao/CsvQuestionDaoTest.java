package ru.otus.hw.dao;

import org.junit.jupiter.api.*;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.MessageSource;
import org.springframework.context.support.StaticMessageSource;
import ru.otus.hw.config.LocaleConfig;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.dao.convert.QuestionEntityConverter;
import ru.otus.hw.exceptions.QuestionReadException;
import ru.otus.hw.service.localization.LocalizedMessagesServiceImpl;
import ru.otus.hw.utils.QuestionTestDataProvider;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

public class CsvQuestionDaoTest {

    @Mock
    private TestFileNameProvider fileNameProvider;

    private AutoCloseable mocks;

    private static MessageSource messageSource;

    private QuestionDao daoUnderTest;

    @BeforeAll
    static void onCreate() {
        StaticMessageSource src = new StaticMessageSource();
        src.addMessage("CsvQuestionDao.error.readquestions", Locale.getDefault(),
                "Error read questions: {0}");
        src.addMessage("CsvQuestionDao.no.questionsfile", Locale.getDefault(),
                "questions resource file not available");
        messageSource = src;
    }

    @BeforeEach
    void setUp() {
        mocks = MockitoAnnotations.openMocks(this);

        LocaleConfig localeConfig = Locale::getDefault;

        daoUnderTest = new CsvQuestionDao(
                fileNameProvider,
                new LocalizedMessagesServiceImpl(localeConfig, messageSource),
                new QuestionEntityConverter()
        );
    }

    @AfterEach
    void tearDown() throws Exception {
        mocks.close();
    }

    @AfterAll
    static void onDestroy() {
        messageSource = null;
    }

    @DisplayName("должен выбрасить исключение, если CSV файл отсутствует")
    @Test
    void mustThrowExceptionOnNoFile() {
        BDDMockito.given(fileNameProvider.getTestFileName()).willReturn("vali-d.csv");


        var exception = assertThrows(
                QuestionReadException.class,
                daoUnderTest::findAll
        );

        assertEquals("Error read questions: questions resource file not available", exception.getMessage());
    }

    @DisplayName("должен выбрасить исключение, если CSV файл содержит невалидные данные")
    @Test
    void mustThrowExceptionOnInvalidData() {
        BDDMockito.given(fileNameProvider.getTestFileName()).willReturn("invalid.csv");

        var exception = assertThrows(
                QuestionReadException.class,
                daoUnderTest::findAll
        );

        var cause = exception.getCause().getCause();
        assertInstanceOf(IllegalArgumentException.class, cause);
        assertEquals("The provided value is not Boolean: troe", cause.getMessage());
    }

    @DisplayName("должен правильно прочитать вопросы из CSV файла")
    @Test
    void readValidCsvResources() throws QuestionReadException {
        BDDMockito.given(fileNameProvider.getTestFileName()).willReturn("valid.csv");


        var realQuestions = daoUnderTest.findAll();
        var expectedQuestions = QuestionTestDataProvider.buildExpectedQuestions();

        assertEquals(expectedQuestions, realQuestions);
    }

}
