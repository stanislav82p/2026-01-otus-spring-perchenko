package ru.otus.hw.utils;

import ru.otus.hw.domain.Question;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;

public class TestUtils {

    public static String referencePrintQuestions(List<Question> questions) {
        var os = new ByteArrayOutputStream(512);
        PrintStream printStream = new PrintStream(os);

        printStream.println("Please answer the questions below");
        for (int qIndex = 1; qIndex <= questions.size(); qIndex++) {
            var q = questions.get(qIndex - 1);
            printStream.printf("\n%d. %s" + "%n", qIndex, q.text());
            for (int aIndex = 1; aIndex <= q.answers().size(); aIndex++) {
                printStream.printf("\t%d.%d. %s" + "%n", qIndex, aIndex, q.answers().get(aIndex - 1).text());
            }
        }

        return new String(os.toByteArray());
    }
}
