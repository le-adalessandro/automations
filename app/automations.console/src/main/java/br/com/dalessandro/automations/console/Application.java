package br.com.dalessandro.automations.console;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import br.com.dalessandro.automations.console.runners.MatchFilesRunner;
import lombok.extern.log4j.Log4j2;

@SpringBootApplication
@Log4j2
public class Application implements CommandLineRunner {

	public static void main(final String[] args) {

		final SpringApplication application = new SpringApplication(Application.class);
		application.setAddCommandLineProperties(false);
		application.run(args);
	}

	@Override
	public void run(final String... args) throws Exception {

		if (args.length < 3) {
			log.info("Quantidade de parametros incorreta");
			System.exit(1);
		}

		final String source = args[0];
		log.info("Source file name ".concat(source));

		final Integer columnIndex = Integer.parseInt(args[1]);
		log.info("Column to match ".concat(columnIndex.toString()));

		final String comparingFiles = args[2];
		log.info("Comparing files name ".concat(comparingFiles));

		MatchFilesRunner.seachContents(source, columnIndex, comparingFiles);

		System.exit(0);
	}

}
