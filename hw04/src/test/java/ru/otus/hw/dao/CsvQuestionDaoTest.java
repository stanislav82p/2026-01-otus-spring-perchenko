package ru.otus.hw.dao;

import org.junit.jupiter.api.*;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.StaticMessageSource;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import ru.otus.hw.config.LocaleConfig;
import ru.otus.hw.config.TestConfig;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.exceptions.QuestionReadException;
import ru.otus.hw.utils.QuestionTestDataProvider;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

@DisplayName("Тест DAO вопросов тестирования: CsvQuestionDao")
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class CsvQuestionDaoTest {

    @TestConfiguration
    public class OverrideBean {

        @Bean
        public MessageSource provideMessageSource() {

            StaticMessageSource src = new StaticMessageSource();
            src.addMessage("CsvQuestionDao.error.readquestions", Locale.getDefault(),
                    "Error read questions: {0}");
            src.addMessage("CsvQuestionDao.no.questionsfile", Locale.getDefault(),
                    "questions resource file not available");
            return src;
        }
    }

    @MockitoBean
    private TestFileNameProvider mockedFileNameProvider;

    @MockitoBean
    private LocaleConfig mockedLocaleConfig;

    @MockitoBean
    private TestConfig mockedTestConfig;

    @Autowired
    private QuestionDao daoUnderTest;

    @BeforeEach
    void setUp() {
        BDDMockito.given(mockedLocaleConfig.getLocale()).willReturn(Locale.getDefault());
        BDDMockito.given(mockedTestConfig.getRightAnswersCountToPass()).willReturn(3);
    }

    @DisplayName("должен выбрасить исключение, если CSV файл отсутствует")
    @Test
    void mustThrowExceptionOnNoFile() {
        BDDMockito.given(mockedFileNameProvider.getTestFileName()).willReturn("vali-d.csv");


        var exception = assertThrows(
                QuestionReadException.class,
                daoUnderTest::findAll
        );

        assertEquals("Error read questions: questions resource file not available", exception.getMessage());
    }

    @DisplayName("должен выбрасить исключение, если CSV файл содержит невалидные данные")
    @Test
    void mustThrowExceptionOnInvalidData() {
        BDDMockito.given(mockedFileNameProvider.getTestFileName()).willReturn("invalid.csv");

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
        BDDMockito.given(mockedFileNameProvider.getTestFileName()).willReturn("valid.csv");


        var realQuestions = daoUnderTest.findAll();
        var expectedQuestions = QuestionTestDataProvider.buildExpectedQuestions();

        assertEquals(expectedQuestions, realQuestions);
    }

}
