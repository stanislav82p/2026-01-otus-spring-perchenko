package ru.otus.hw.domain;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.tuple.ImmutablePair;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

public class TestResult {

    @Getter
    private final Student student;

    @Getter
    @Setter
    private boolean isTestRun;

    private final List<Entry<Question, Boolean>> answeredQuestions;

    public TestResult(Student student) {
        this.student = student;
        this.answeredQuestions = new ArrayList<>();
    }

    public void applyAnswer(Question question, boolean isRightAnswer) {
        answeredQuestions.add(new ImmutablePair<>(question, isRightAnswer));
    }

    public int getNumAnsweredQuestions() {
        return answeredQuestions.size();
    }

    public int getNumRightAnsweredQuestions() {
        return answeredQuestions.stream()
                .filter(it -> it.getValue())
                .mapToInt(it -> 1)
                .sum();
    }

    public List<Entry<Question, Boolean>> getAllAnsweredQuestions() {
        var copy = new ArrayList<Entry<Question, Boolean>>(answeredQuestions.size());
        copy.addAll(answeredQuestions);
        return copy;
    }

    public List<Question> getRightAnsweredQuestions() {
        return answeredQuestions.stream()
                .filter(Entry::getValue)
                .map(Entry::getKey)
                .toList();
    }
}
