package ru.otus.hw.service.io;

import org.junit.jupiter.api.*;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.context.MessageSource;
import org.springframework.context.support.StaticMessageSource;
import ru.otus.hw.config.LocaleConfig;
import ru.otus.hw.service.localization.LocalizedMessagesService;
import ru.otus.hw.service.localization.LocalizedMessagesServiceImpl;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Locale;

public class StreamsIOServiceTest {

    private static LocalizedMessagesService messagesService;

    @Mock
    private PrintStream printStream;

    @Mock
    private InputStream inputStream;

    private AutoCloseable mocks;

    @BeforeAll
    static void onCreate() {
        LocaleConfig localeConfig = () -> Locale.ENGLISH;

        MessageSource src = Mockito.mock(MessageSource.class);

        messagesService = new LocalizedMessagesServiceImpl(localeConfig, src);
    }

    @BeforeEach
    void setUp() {
        mocks = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception {
        mocks.close();
    }

    @AfterAll
    static void onDestroy() {
        messagesService = null;
    }

    @DisplayName("проверяем вывод неформатированной строки")
    @Test
    void testNonFormatted() {
        IOService service = new StreamsIOService(printStream, inputStream, messagesService);

        service.printLine("test text.");

        var captor = ArgumentCaptor.forClass(String.class);
        Mockito.verify(printStream, Mockito.times(1)).println(captor.capture());

        assertEquals("test text.", captor.getValue());
    }

    @DisplayName("проверяем форматированный вывод")
    @Test
    void testFormatted() {
        var os = new ByteArrayOutputStream(100);
        IOService service = new StreamsIOService(new PrintStream(os), inputStream, messagesService);

        service.printFormattedLine("test text - %d (%s).", 5, "123");

        var actualPrinted = new String(os.toByteArray());

        assertEquals("test text - 5 (123).\r\n", actualPrinted);
    }
}
