package ru.otus.hw.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Question;
import ru.otus.hw.utils.QuestionTestDataProvider;
import ru.otus.hw.utils.TestUtils;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestServiceImplTest {

    @Mock
    private QuestionDao dao;


    private AutoCloseable mocks;

    @BeforeEach
    void setUp() {
        mocks = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception {
        mocks.close();
    }

    @DisplayName("Должен правильно печатать вопросы с ответами")
    @Test
    void mustPrintExpectedData() {
        List<Question> testQuestions = QuestionTestDataProvider.buildExpectedQuestions();
        String expectedPrint = TestUtils.referencePrintQuestions(testQuestions);

        BDDMockito.given(dao.findAll()).willReturn(testQuestions);

        var os = new ByteArrayOutputStream(512);
        var ioService = new StreamsIOService(new PrintStream(os));


        var serviceToTest = new TestServiceImpl(dao, ioService);
        serviceToTest.executeTest();

        var actualPrint = new String(os.toByteArray());

        assertEquals(expectedPrint, actualPrint);
    }
}
