package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Question;
import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.TestResult;
import ru.otus.hw.exceptions.NotLoggedInException;
import ru.otus.hw.service.io.LocalizedIOService;
import ru.otus.hw.service.localization.LocalizedMessagesService;
import ru.otus.hw.service.mix.EntityMixer;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

    private final LocalizedIOService ioService;

    private final LocalizedMessagesService resources;

    private final QuestionDao questionDao;

    private final EntityMixer entityMixer;

    @Override
    public TestResult executeTestFor(Student student) {
        if (student == null) {
            throw new NotLoggedInException();
        }

        var testResult = new TestResult(student);
        List<Question> questions = questionDao.findAll();
        var mixedQuestion = entityMixer.mixEntityList(questions);

        ioService.printLine("");
        ioService.printLineLocalized("TestService.answer.the.questions");
        ioService.printLine("");

        int index = 0;
        for (Question question: mixedQuestion) {
            askQuestion(question, ++ index, questions.size());
            getAnswer(question, testResult);
        }
        return testResult;
    }

    private void askQuestion(Question question, int number, int totalQuestionsNumber) {
        ioService.printFormattedLine("\n%d of %d.  %s", number, totalQuestionsNumber, question.text());
        int index = 0;
        for (var answer: question.answers()) {
            ioService.printFormattedLine("\t%d. %s", ++ index, answer.text());
        }
    }

    private void getAnswer(Question question, TestResult testResult) {
        try {
            var errorMessage = resources.getMessage("TestService.try.again", 1, question.answers().size());
            int variantNumber = ioService.readIntForRange(1, question.answers().size(), errorMessage);
            testResult.applyAnswer(question, question.answers().get(variantNumber - 1).isCorrect());
            ioService.printLineLocalized("TestService.answer.accepted");
        } catch (Throwable t) {
            ioService.printLineLocalized("TestService.error.read.answer");
            testResult.applyAnswer(question, false);
        }
    }

}
