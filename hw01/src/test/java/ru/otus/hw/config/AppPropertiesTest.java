package ru.otus.hw.config;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class AppPropertiesTest {

    @DisplayName("должен вернуть ожидаемое имя файла")
    @ParameterizedTest
    @ValueSource(strings = {"test1", "questions.xml"})
    void mustReturnExpectedValue(String fileName) {
        var appProperties = new AppProperties(fileName);

        assertEquals(appProperties.testFileName(), fileName);
    }

    @DisplayName("должен выкинуть исключение, если имя файла null")
    @Test
    void mustThrowExceptionOnNull() {
        var exception = assertThrows(
                IllegalArgumentException.class,
                () -> new AppProperties(null)
        );
        assertEquals("Test file name must not be NULL", exception.getMessage());
    }

    @DisplayName("должен выкинуть исключение, если имя файла не допустимого формата")
    @ParameterizedTest
    @ValueSource(strings = {"", "   ", "two words"})
    void mustThrowExceptionOnInvalidValue(String fileName) {
        var exception = assertThrows(
                IllegalArgumentException.class,
                () -> new AppProperties(fileName)
        );
        if (fileName.isBlank()) {
            assertEquals("Test file name must not be empty or blank", exception.getMessage());
        } else {
            assertEquals("Test file name must not be multiple words", exception.getMessage());
        }
    }

}
