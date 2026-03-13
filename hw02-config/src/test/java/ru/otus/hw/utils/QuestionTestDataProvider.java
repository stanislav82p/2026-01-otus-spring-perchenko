package ru.otus.hw.utils;

import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;

import java.util.ArrayList;
import java.util.List;

public class QuestionTestDataProvider {

    public static List<Question> buildExpectedQuestions() {
        List<Question> questions = new ArrayList<>(2);
        questions.add(buildQuestion1());
        questions.add(buildQuestion2());
        return questions;
    }

    private static Question buildQuestion1() {
        List<Answer> answers = new ArrayList<>();
        answers.add(new Answer("Correct answer for question 1", true));
        answers.add(new Answer("Incorrect answer 1 for question 1", false));
        answers.add(new Answer("Incorrect answer 2 for question 1", false));
        return new Question("Test question 1?", answers);
    }

    private static Question buildQuestion2() {
        List<Answer> answers = new ArrayList<>();
        answers.add(new Answer("Incorrect answer for question 2", false));
        answers.add(new Answer("Correct answer for question 2", true));
        return new Question("Test question 2?", answers);
    }
}
