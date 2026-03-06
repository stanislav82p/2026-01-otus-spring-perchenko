package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;

import java.util.concurrent.atomic.AtomicInteger;

@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

    private final QuestionDao questionDao;

    private final IOService ioService;

    @Override
    public void executeTest() {
        ioService.printLine("Please answer the questions below");
        AtomicInteger questionIndex = new AtomicInteger();
        questionDao.findAll().forEach((Question q) -> {
            printIndexedQuestion(questionIndex.getAndIncrement(), q);
        });
    }

    private void printIndexedQuestion(int index, Question q) {
        AtomicInteger answIndex = new AtomicInteger();
        ioService.printFormattedLine("\n%d. %s", (index + 1), q.text());
        q.answers().forEach((Answer answ) -> {
            ioService.printFormattedLine("\t%d.%d. %s", (index + 1), answIndex.incrementAndGet(), answ.text());
        });
    }
}
