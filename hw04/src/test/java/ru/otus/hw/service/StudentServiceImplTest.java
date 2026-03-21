package ru.otus.hw.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.otus.hw.domain.Student;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Тест сервиса логина студентов: StudentServiceImpl")
@SpringBootTest(classes = StudentServiceImpl.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class StudentServiceImplTest {

    @Autowired
    private StudentService studentService;

    @DisplayName("Не должен быть залогинен сразу после создания")
    @Test
    void mustNotLoggedInOnCreation() {
        assertFalse(studentService.isLoggedIn());
        assertNull(studentService.getCurrentStudent());
    }

    @DisplayName("Должен логиниться")
    @Test
    void mustLogin() {
        studentService.login("Stanislav", "Perchenko");

        assertTrue(studentService.isLoggedIn());
        assertEquals(new Student("Stanislav", "Perchenko"), studentService.getCurrentStudent());
    }

    @DisplayName("Должен логиниться поверх логина")
    @Test
    void mustLoginAfterLogin() {
        studentService.login("Вася", "Иванов");
        studentService.login("Stanislav", "Perchenko");

        assertTrue(studentService.isLoggedIn());
        assertEquals(new Student("Stanislav", "Perchenko"), studentService.getCurrentStudent());
    }

    @DisplayName("Должен разлогиниваться")
    @Test
    void mustLogout() {
        studentService.login("Stanislav", "Perchenko");
        studentService.logout();

        assertFalse(studentService.isLoggedIn());
        assertNull(studentService.getCurrentStudent());
    }
}
