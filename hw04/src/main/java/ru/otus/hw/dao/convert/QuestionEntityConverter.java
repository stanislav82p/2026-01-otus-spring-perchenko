package ru.otus.hw.dao.convert;

import org.springframework.stereotype.Component;
import ru.otus.hw.dao.dto.QuestionDto;
import ru.otus.hw.domain.Question;
import ru.otus.hw.utils.EntityConverter;

@Component
public class QuestionEntityConverter implements EntityConverter<QuestionDto, Question> {

    @Override
    public Question convert(QuestionDto from) {
        return new Question(from.getText(), from.getAnswers());
    }
}
