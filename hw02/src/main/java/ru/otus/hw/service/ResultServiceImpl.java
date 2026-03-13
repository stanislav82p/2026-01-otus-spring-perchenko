package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.config.TestConfig;
import ru.otus.hw.domain.TestResult;
import ru.otus.hw.service.io.IOService;

@Service
@RequiredArgsConstructor
public class ResultServiceImpl implements ResultService {

    private final TestConfig testConfig;

    private final IOService ioService;

    @Override
    public void showResult(TestResult testResult) {
        ioService.printLine("");
        ioService.printLine("Test results: ");
        ioService.printFormattedLine("Student: %s", testResult.getStudent().getFullName());
        ioService.printFormattedLine("Answered questions count: %d", testResult.getNumAnsweredQuestions());
        ioService.printFormattedLine("Correct answers count: %d", testResult.getNumRightAnsweredQuestions());

        if (testResult.getNumRightAnsweredQuestions() >= testConfig.getRightAnswersCountToPass()) {
            ioService.printLine("Congratulations! You passed test!");
            return;
        }
        ioService.printFormattedLine(
                "Sorry, you failed the test. Minimum correct answers to pass is %d",
                testConfig.getRightAnswersCountToPass()
        );
    }
}
