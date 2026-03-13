package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Question;
import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.TestResult;
import ru.otus.hw.service.io.IOService;
import ru.otus.hw.service.mix.EntityMixer;

@RequiredArgsConstructor
public class TestServiceImpl implements TestService {
    public static final String TRY_AGAIN_MESSAGE_TEMPLATE = "Вы должны ввести значение от %d до %d включительно.";

    public static final String ERROR_READ_ANSWER_MESSAGE =
            "Вы все равно ввели значение не из предложенного диапазона. Ответ не засчитан!";

    public static final String ANSWER_ACCEPTED_MESSAGE = "Ответ принят.";

    private final IOService ioService;

    private final QuestionDao questionDao;

    private final EntityMixer entityMixer;

    @Override
    public TestResult executeTestFor(Student student) {
        ioService.printLine("\n========  Please answer the questions below  ========");
        var questions = questionDao.findAll();
        var mixedQuestion = entityMixer.mixEntityList(questions);
        var testResult = new TestResult(student);

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
            int variantNumber = ioService.readIntForRange(1, question.answers().size(), TRY_AGAIN_MESSAGE_TEMPLATE);
            testResult.applyAnswer(question, question.answers().get(variantNumber - 1).isCorrect());
            ioService.printLine(ANSWER_ACCEPTED_MESSAGE);
        } catch (Throwable t) {
            ioService.printLine(ERROR_READ_ANSWER_MESSAGE);
            testResult.applyAnswer(question, false);
        }
    }
}
