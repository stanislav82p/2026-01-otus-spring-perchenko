package ru.otus.hw.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Getter
public class AppProperties implements TestConfig, TestFileNameProvider {

    private final int rightAnswersCountToPass;

    private final String testFileName;

    public AppProperties(
            @Value("${test.rightAnswersCountToPass:1}") int rightAnswersCountToPass,
            @Value("${test.fileName}") String testFileName
    ) {
        this.rightAnswersCountToPass = rightAnswersCountToPass;
        this.testFileName = testFileName;
    }
}
