package ru.otus.hw.dao.convert;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import ru.otus.hw.dao.dto.QuestionDto;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;
import ru.otus.hw.utils.EntityConverter;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;

@SpringBootTest
@ContextConfiguration(classes = QuestionEntityConverter.class)
public class QuestionEntityConverterTest {

    @Autowired
    private EntityConverter<QuestionDto, Question> converter;

    @DisplayName("должен выбрасывать исключение, если на входже NULL")
    @Test
    void mustThrowExceptionIfNull() {
        assertThrows(
                NullPointerException.class,
                () -> converter.convert(null)
        );
    }

    @DisplayName("должен вернуть ожидаемое значение Question")
    @Test
    void mustConvert() {
        var answers = new ArrayList<Answer>(2);
        answers.add(new Answer("Correct answer", true));
        answers.add(new Answer("Incorrect answer", false));
        var dto = new QuestionDto();
        dto.setText("Test question");
        dto.setAnswers(answers);

        Question converted = converter.convert(dto);

        assertEquals(dto.getText(), converted.text());
        assertIterableEquals(answers, converted.answers());
    }
}
