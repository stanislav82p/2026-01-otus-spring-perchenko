package ru.otus.hw.dao.convert;

import com.opencsv.bean.AbstractCsvConverter;
import ru.otus.hw.domain.Answer;

public class AnswerCsvConverter extends AbstractCsvConverter {

    @Override
    public Object convertToRead(String value) {
        var valueArr = value.split("%");

        // Мы не будем тут использовать Boolean.parseBoolean(), потому что он парсит нестрого (svperchenko)
        if ("true".equalsIgnoreCase(valueArr[1])) {
            return new Answer(valueArr[0], true);
        } else if ("false".equalsIgnoreCase(valueArr[1])) {
            return new Answer(valueArr[0], false);
        } else {
            throw new IllegalArgumentException("The provided value is not Boolean: " + valueArr[1]);
        }
    }
}
