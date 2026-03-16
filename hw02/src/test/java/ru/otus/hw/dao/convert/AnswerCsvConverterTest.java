package ru.otus.hw.dao.convert;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import ru.otus.hw.domain.Answer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

public class AnswerCsvConverterTest {

    private AnswerCsvConverter converter;

    @BeforeEach
    void setUp() {
        converter = new AnswerCsvConverter();
    }

    @DisplayName("должен выбрасывать исключение, если текст NULL")
    @Test
    void mustThrowExceptionOnNull() {
        var exception = assertThrows(
                NullPointerException.class,
                () -> converter.convertToRead(null)
        );
    }

    @DisplayName("должен выбрасывать исключение, если boolean отсутствует")
    @ParameterizedTest
    @ValueSource(strings = {"Test answer.", "Test answer.%"})
    void mustThrowExceptionWhenCorrectFlag(String answer) {
        var exception = assertThrows(
                ArrayIndexOutOfBoundsException.class,
                () -> converter.convertToRead(answer)
        );
    }

    @DisplayName("должен выбрасывать исключение, если boolean неверного фыормата")
    @ParameterizedTest
    @ValueSource(strings = {"Test answer.% ", "Test answer.%folse"})
    void mustThrowExceptionWhenWrongBooleanFormat(String answer) {
        var exception = assertThrows(
                IllegalArgumentException.class,
                () -> converter.convertToRead(answer)
        );
        assertEquals("The provided value is not Boolean: " + answer.split("%")[1], exception.getMessage());
    }

    @DisplayName("должен возвращать ожидаемый объект ответа")
    @ParameterizedTest
    @ValueSource(strings = {"The correct answer.%true", "The incorrect answer.%false"})
    void mustReturnExpectedValue(String text) {
        Object answer = converter.convertToRead(text);
        String[] parts = text.split("%");
        boolean isCorrect = Boolean.parseBoolean(parts[1]);

        assertInstanceOf(Answer.class, answer);

        assertEquals(parts[0], ((Answer) answer).text());
        assertEquals(isCorrect, ((Answer) answer).isCorrect());
    }

}
