package ru.otus.hw;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.shell.command.annotation.CommandScan;

@CommandScan
@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(Application.class, args);

		// Эти проверки использовались при разработке
		// (не забыть в application.yaml - shell.interactive.enabled: false)
		/* var tester = context.getBean(Tester.class);
		tester.checkComments(context);
		tester.checkAuthor(context);
		tester.checkGenre(context);
		tester.checkBooks(context);*/
	}



}

