package ru.otus.hw.dao;

import com.opencsv.bean.CsvToBeanBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.dao.dto.QuestionDto;
import ru.otus.hw.domain.Question;
import ru.otus.hw.exceptions.QuestionReadException;
import ru.otus.hw.service.localization.LocalizedMessagesService;
import ru.otus.hw.utils.EntityConverter;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class CsvQuestionDao implements QuestionDao {
    private final TestFileNameProvider fileNameProvider;

    private final LocalizedMessagesService resources;

    private final EntityConverter<QuestionDto, Question> questionConverter;

    @Override
    public List<Question> findAll() throws QuestionReadException {
        try (Reader reader = getQuestionStorageReader(fileNameProvider.getTestFileName())) {
            return new CsvToBeanBuilder<QuestionDto>(reader)
                    .withType(QuestionDto.class)
                    .withSeparator(';')
                    .withSkipLines(1)
                    .build()
                    .parse()
                    .stream()
                    .map(questionConverter::convert)
                    .toList();
        } catch (IOException | RuntimeException ex) {
            var causeMessage = (ex.getCause() != null) ? ex.getCause().getMessage() : ex.getMessage();
            var errorMessage = resources.getMessage("CsvQuestionDao.error.readquestions", causeMessage);
            throw new QuestionReadException(errorMessage, ex);
        }
    }

    private Reader getQuestionStorageReader(String fileName) throws QuestionReadException {
        InputStream is = getClass().getClassLoader().getResourceAsStream(fileName);
        if (is == null) {
            throw new QuestionReadException(resources.getMessage("CsvQuestionDao.no.questionsfile"));
        } else {
            return new InputStreamReader(is);
        }
    }
}
