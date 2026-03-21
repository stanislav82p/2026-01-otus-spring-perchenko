package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.exceptions.NotLoggedInException;

@Service
@RequiredArgsConstructor
public class TestRunnerServiceImpl implements TestRunnerService {

    private final StudentService studentService;

    private final TestService testService;

    private final ResultService resultService;

    @Override
    public void run() {
        if (!studentService.isLoggedIn()) {
            throw new NotLoggedInException();
        }

        var student = studentService.getCurrentStudent();
        var testResult = testService.executeTestFor(student);
        resultService.showResult(testResult);
    }
}
