package ru.otus.hw.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.MessageSource;
import org.springframework.context.support.StaticMessageSource;
import ru.otus.hw.config.LocaleConfig;
import ru.otus.hw.domain.Student;
import ru.otus.hw.service.io.IOService;
import ru.otus.hw.service.io.LocalizedIOService;
import ru.otus.hw.service.io.LocalizedIOServiceImpl;
import ru.otus.hw.service.io.StreamsIOService;
import ru.otus.hw.service.localization.LocalizedMessagesService;
import ru.otus.hw.service.localization.LocalizedMessagesServiceImpl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StudentServiceImplTest {

    private LocalizedMessagesService messagesService;

    @BeforeEach
    void setUp() {
        LocaleConfig localeConfig = () -> Locale.ENGLISH;

        StaticMessageSource src = new StaticMessageSource();
        src.addMessage("StudentService.input.first.name", localeConfig.getLocale(),
                "Please, input your first name");
        src.addMessage("StudentService.input.last.name", localeConfig.getLocale(),
                "Please, input your last name");

        messagesService = new LocalizedMessagesServiceImpl(localeConfig, src);
    }

    @DisplayName("Проверить печать приглашений и ввод имени")
    @Test
    void mustPrintPromptsAndGetData() {
        String testInpData = String.format("Stanislav%1$sPerchenko%1$s", System.lineSeparator());
        var in = new ByteArrayInputStream(testInpData.getBytes());
        var out = new ByteArrayOutputStream();

        LocalizedIOService ioService = new LocalizedIOServiceImpl(
                messagesService,
                new StreamsIOService(new PrintStream(out), in, messagesService)
        );

        StudentServiceImpl ss = new StudentServiceImpl(ioService);

        var student = ss.determineCurrentStudent();

        var outText = new String(out.toByteArray());

        var refStudent = new Student("Stanislav", "Perchenko");
        var refOutText = String.format(
                "Please, input your first name%1$sPlease, input your last name%1$s",
                System.lineSeparator()
        );

        assertEquals(refStudent, student);
        assertEquals(refOutText, outText);
    }

}
