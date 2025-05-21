package com.rassteba.factorial_project;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

@Log4j2
@Component
@RequiredArgsConstructor
@SpringBootApplication
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FactorialProjectApplication implements CommandLineRunner {

	InputReader inputReader;
	OutputWriter outputWriter;
	FactorialProcessor processor;

	public static void main(String[] args) {
		log.info("Starting application...");
		SpringApplication.run(FactorialProjectApplication.class, args);
		log.info("Application finished.");
	}

	@Override
	public void run(String... args) {
		Scanner scanner = new Scanner(System.in);
		log.info("Enter number of threads in pool: ");
		int poolSize = scanner.nextInt();
		scanner.close();

		String inputFile = "input.txt";
		String outputFile = "output.txt";

		processor.configure(poolSize, 100.0);

		try {
			List<String> lines = inputReader.readLines(inputFile);
			List<String> results = processor.process(lines);
			outputWriter.writeResults(results, outputFile);
			log.info("Done! The results are written in {}", outputFile);
		} catch (IOException e) {
			log.error("File error: {}", e.getMessage());
		}
	}
}
