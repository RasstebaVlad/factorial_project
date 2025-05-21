package com.rassteba.factorial_project.api;

import com.google.common.util.concurrent.RateLimiter;

import java.io.BufferedWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class FactorialOrderedOutput {
  public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);
    System.out.print("Enter number of threads in pool: ");
    int poolSize = scanner.nextInt();
    scanner.close();

    String inputFile = "input.txt";
    String outputFile = "output.txt";

    List<String> lines;
    try {
      lines = Files.readAllLines(Paths.get(inputFile));
    } catch (IOException e) {
      System.err.println("Error reading file: " + e.getMessage());
      return;
    }

    ExecutorService executor = Executors.newFixedThreadPool(poolSize);
    RateLimiter rateLimiter = RateLimiter.create(100.0);
    List<Future<String>> futures = new ArrayList<>();

    for (String line : lines) {
      rateLimiter.acquire();
      Callable<String> task = createTask(line);
      futures.add(executor.submit(task));
    }

    executor.shutdown();

    try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(outputFile))) {
      for (Future<String> future : futures) {
        try {
          String result = future.get();
          writer.write(result);
          writer.newLine();
        } catch (InterruptedException | ExecutionException e) {
          writer.write("Calculation error: " + e.getMessage());
          writer.newLine();
        }
      }
    } catch (IOException e) {
      System.err.println("Error writing to file: " + e.getMessage());
    }

    try {
      if (!executor.awaitTermination(1, TimeUnit.MINUTES)) {
        executor.shutdownNow();
      }
    } catch (InterruptedException e) {
      executor.shutdownNow();
    }

    System.out.println("Done! The results are written in" + outputFile);
  }

  private static BigInteger factorial(int n) {
    if (n < 0) {
      throw new IllegalArgumentException("The number cannot be negative");
    }
    BigInteger result = BigInteger.ONE;
    for (int i = 2; i <= n; i++) {
      result = result.multiply(BigInteger.valueOf(i));
    }
    return result;
  }

  private static Callable<String> createTask(String line) {
    return () -> {
      try {
        int num = Integer.parseInt(line.trim());
        BigInteger fact = factorial(num);
        return num + " = " + fact;
      } catch (NumberFormatException e) {
        return "Invalid number format: " + line;
      } catch (IllegalArgumentException e) {
        return "Error: " + e.getMessage();
      }
    };
  }

}
