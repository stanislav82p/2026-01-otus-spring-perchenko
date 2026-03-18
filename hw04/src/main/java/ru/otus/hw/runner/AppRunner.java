package ru.otus.hw.runner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import ru.otus.hw.service.TestRunnerService;

@Component
public class AppRunner implements ApplicationRunner {

    private final TestRunnerService testRunner;

    @Autowired
    public AppRunner(TestRunnerService testRunner) {
        this.testRunner = testRunner;
    }

    @Override
    public void run(ApplicationArguments args) {
        testRunner.run();
    }
}
