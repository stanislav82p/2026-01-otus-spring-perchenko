package ru.otus.hw.shell;

import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.jline.terminal.Terminal;
import org.springframework.shell.ParameterValidationException;
import org.springframework.shell.command.annotation.ExceptionResolver;
import org.springframework.shell.command.annotation.ExitCode;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.otus.hw.domain.Student;
import ru.otus.hw.exceptions.NotLoggedInException;
import ru.otus.hw.exceptions.QuestionReadException;
import ru.otus.hw.service.StudentService;
import ru.otus.hw.service.TestRunnerService;
import ru.otus.hw.service.localization.LocalizedMessagesService;

@ShellComponent(value = "Команды тестирования")
@RequiredArgsConstructor
public class AppCommands {

    private final StudentService studentService;

    private final TestRunnerService testRunner;

    private final LocalizedMessagesService resources;

    @ShellMethod(value = "Login command", key = "login")
    public String logIn(
            @ShellOption(value = { "--first-name", "-f" }, help = "Имя (только буквы, от 3 символов, с большой буквы)")
            @Pattern(regexp = "^(?:[A-Z][a-z]{2,}|[А-ЯЁ][а-яё]{2,})$")
            String firstName,

            @ShellOption(
                    value = { "--last-name",  "-l" },
                    help = "Фамилия (только буквы, от 3 символов, с большой буквы)"
            )
            @Pattern(regexp = "^(?:[A-Z][a-z]{2,}|[А-ЯЁ][а-яё]{2,})$")
            String lastName
    ) {
        Student stud = studentService.login(firstName, lastName);
        return resources.getMessage("AppCommands.login.hello", stud.getFullName());
    }

    @ShellMethod(value = "Logout command", key = "logout")
    public String logOut() {
        Student stud = studentService.logout();
        return (stud == null) ? "" : resources.getMessage("AppCommands.logout.goodbye", stud.getFullName());
    }

    @ShellMethod(value = "Run test command", key = "test")
    public String runTest() {
        testRunner.run();
        return null;
    }

    @ExceptionResolver({ NotLoggedInException.class })
    @ExitCode(2)
    void noLoginErrorHandler(Terminal t) {
        printColored(t, resources.getMessage("AppCommands.test.error.notlogin"), Color.ORANGE);
    }

    @ExceptionResolver({ QuestionReadException.class })
    @ExitCode(3)
    void loadQuestionsErrorHandler(Exception e, Terminal t) {
        String message = resources.getMessage("AppCommands.questions.not.loaded", e.getMessage());
        printColored(t, message, Color.RED);
    }

    @ExceptionResolver({ ParameterValidationException.class })
    @ExitCode(4)
    void parameterValidationErrorHandler(Exception e, Terminal t) {
        String message = resources.getMessage("AppCommands.parameter.validation.error");
        printColored(t, message, Color.RED);
    }

    @ExceptionResolver({ RuntimeException.class })
    @ExitCode(1)
    void commonErrorHandler(Exception e, Terminal t) {
        String message = resources.getMessage("AppCommands.test.error.common", e.getMessage());
        printColored(t, message, Color.RED);
    }

    private void printColored(Terminal t, String msg, Color c) {
        var text = String.format("%s%s%s", c.ansiCode, msg, Color.NONE.ansiCode);
        var writer = t.writer();
        writer.println(text);
        writer.flush();
    }

    enum Color {
        RED("\033[31m"), ORANGE("\033[38;5;208m"), NONE("\033[0m");

        private final String ansiCode;

        Color(String ansiCode) {
            this.ansiCode = ansiCode;
        }
    }
}
