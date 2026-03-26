package ru.otus.hw.service;

import org.springframework.stereotype.Service;
import ru.otus.hw.domain.Student;

@Service
public class StudentServiceImpl implements StudentService {

    private Student currentStudent;

    @Override
    public synchronized Student login(String firstName, String secondName) {
        return currentStudent = new Student(firstName, secondName);
    }

    @Override
    public synchronized Student logout() {
        if (currentStudent != null) {
            var stud = currentStudent;
            currentStudent = null;
            return stud;
        } else {
            return null;
        }
    }

    @Override
    public synchronized boolean isLoggedIn() {
        return currentStudent != null;
    }

    @Override
    public synchronized Student getCurrentStudent() {
        return currentStudent;
    }
}
