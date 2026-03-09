package ru.otus.hw.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.hw.domain.Student;
import ru.otus.hw.service.io.IOService;
import ru.otus.hw.service.io.StreamsIOService;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StudentServiceImplTest {

    @DisplayName("Проверить печать приглашений и ввод имени")
    @Test
    void mustPrintPromptsAndGetData() {
        String testInpData = String.format("Stanislav%1$sPerchenko%1$s", System.lineSeparator());
        var in = new ByteArrayInputStream(testInpData.getBytes());
        var out = new ByteArrayOutputStream();

        IOService ioService = new StreamsIOService(new PrintStream(out), in);

        StudentServiceImpl ss = new StudentServiceImpl(ioService);

        var student = ss.determineCurrentStudent();

        var outText = new String(out.toByteArray());

        var refStudent = new Student("Stanislav", "Perchenko");
        var refOutText = String.format(
                "Please input your first name%1$sPlease input your last name%1$s",
                System.lineSeparator()
        );

        assertEquals(refStudent, student);
        assertEquals(refOutText, outText);
    }

}
