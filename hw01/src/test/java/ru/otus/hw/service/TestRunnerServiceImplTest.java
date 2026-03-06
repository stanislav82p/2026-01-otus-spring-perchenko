package ru.otus.hw.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Mockito;

public class TestRunnerServiceImplTest {

    @Mock
    private TestService testService;

    private AutoCloseable mocks;

    @BeforeEach
    void setUp() {
        mocks = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception {
        mocks.close();
    }

    @DisplayName("должен быть вызван метод run() от TestService")
    @Test
    void mustCallRun() {
        TestRunnerService runner = new TestRunnerServiceImpl(testService);

        runner.run();

        Mockito.verify(testService, Mockito.times(1)).executeTest();
    }
}
