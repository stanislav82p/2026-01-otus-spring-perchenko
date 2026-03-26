package ru.otus.hw.dao;

import ru.otus.hw.domain.Question;
import ru.otus.hw.exceptions.QuestionReadException;

import java.util.List;

public interface QuestionDao {
    List<Question> findAll() throws QuestionReadException;
}
