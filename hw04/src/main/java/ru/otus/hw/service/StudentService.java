package ru.otus.hw.service;

import org.springframework.lang.Nullable;
import ru.otus.hw.domain.Student;

public interface StudentService {

    Student login(String firstName, String secondName);

    @Nullable
    Student logout();

    boolean isLoggedIn();

    @Nullable
    Student getCurrentStudent();
}
