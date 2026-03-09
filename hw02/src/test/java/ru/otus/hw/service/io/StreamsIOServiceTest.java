package ru.otus.hw.service.io;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;

public class StreamsIOServiceTest {

    @Mock
    private PrintStream printStream;

    @Mock
    private InputStream inputStream;

    private AutoCloseable mocks;

    @BeforeEach
    void setUp() {
        mocks = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception {
        mocks.close();
    }

    @DisplayName("проверяем вывод неформатированной строки")
    @Test
    void testNonFormatted() {
        IOService service = new StreamsIOService(printStream, inputStream);

        service.printLine("test text.");

        var captor = ArgumentCaptor.forClass(String.class);
        Mockito.verify(printStream, Mockito.times(1)).println(captor.capture());

        assertEquals("test text.", captor.getValue());
    }

    @DisplayName("проверяем форматированный вывод")
    @Test
    void testFormatted() {
        var os = new ByteArrayOutputStream(100);
        IOService service = new StreamsIOService(new PrintStream(os), inputStream);

        service.printFormattedLine("test text - %d (%s).", 5, "123");

        var actualPrinted = new String(os.toByteArray());

        assertEquals("test text - 5 (123).\r\n", actualPrinted);
    }
}
