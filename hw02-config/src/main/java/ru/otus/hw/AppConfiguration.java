package ru.otus.hw;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.otus.hw.config.AppProperties;
import ru.otus.hw.config.TestConfig;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.dao.CsvQuestionDao;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.dao.convert.QuestionEntityConverter;
import ru.otus.hw.dao.dto.QuestionDto;
import ru.otus.hw.domain.Question;
import ru.otus.hw.service.ResultService;
import ru.otus.hw.service.ResultServiceImpl;
import ru.otus.hw.service.StudentService;
import ru.otus.hw.service.StudentServiceImpl;
import ru.otus.hw.service.TestRunnerService;
import ru.otus.hw.service.TestRunnerServiceImpl;
import ru.otus.hw.service.TestService;
import ru.otus.hw.service.TestServiceImpl;
import ru.otus.hw.service.io.IOService;
import ru.otus.hw.service.io.StreamsIOService;
import ru.otus.hw.service.mix.EntityMixer;
import ru.otus.hw.service.mix.RandomEntityMixer;
import ru.otus.hw.utils.EntityConverter;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.Random;

@Configuration
public class AppConfiguration {

    @Bean("AppProperties")
    public TestConfig provideTestConfig(
            @Value("${test.rightAnswersCountToPass:1}") int rightAnswersCountToPass,
            @Value("${test.fileName}") String testFileName
    ) {
        return new AppProperties(rightAnswersCountToPass, testFileName);
    }

    @Bean("AppProperties")
    public TestFileNameProvider provideTestFileNameProvider(
            @Value("${test.rightAnswersCountToPass:1}") int rightAnswersCountToPass,
            @Value("${test.fileName}") String testFileName
    ) {
        return new AppProperties(rightAnswersCountToPass, testFileName);
    }

    @Bean("questionEntityConverter")
    public EntityConverter<QuestionDto, Question> provideQuestionEntityConverter() {
        return new QuestionEntityConverter();
    }

    @Bean("questionDao")
    public QuestionDao provideQuestionDao(
            TestFileNameProvider fileNameProvider,
            EntityConverter<QuestionDto, Question> questionConverter
    ) {
        return new CsvQuestionDao(fileNameProvider, questionConverter);
    }

    @Bean("streamsIOService")
    public IOService provideStreamsIOService(
            @Value("#{T(System).out}") PrintStream os,
            @Value("#{T(System).in}") InputStream is
    ) {
        return new StreamsIOService(os, is);
    }

    @Bean("testService")
    public TestService provideTestService(
            IOService ioService,
            QuestionDao questionDao,
            EntityMixer entityMixer
    ) {
        return new TestServiceImpl(ioService, questionDao, entityMixer);
    }

    @Bean("studentService")
    public StudentService provideStudentService(IOService ioService) {
        return new StudentServiceImpl(ioService);
    }

    @Bean("resultService")
    public ResultService provideResultService(
            TestConfig testConfig,
            IOService ioService
    ) {
        return new ResultServiceImpl(testConfig, ioService);
    }

    @Bean("testRunnerService")
    public TestRunnerService provideTestRunnerService(
            TestService testService,
            StudentService studentService,
            ResultService resultService
    ) {
        return new TestRunnerServiceImpl(testService, studentService, resultService);
    }

    @Bean("entityMixer")
    public EntityMixer provideEntityMixer() {
        return new RandomEntityMixer(new Random());
    }
}
