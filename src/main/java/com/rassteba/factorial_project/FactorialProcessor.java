package com.rassteba.factorial_project;

import com.google.common.util.concurrent.RateLimiter;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FactorialProcessor {

  ExecutorService executor;
  RateLimiter rateLimiter;
  final FactorialCalculator calculator;

  public void configure(int poolSize, double rateLimitPerSecond) {
    this.executor = Executors.newFixedThreadPool(poolSize);
    this.rateLimiter = RateLimiter.create(rateLimitPerSecond);
  }

  public List<String> process(List<String> inputs) {
    List<Future<String>> futures = new ArrayList<>();
    for (String line : inputs) {
      rateLimiter.acquire();
      futures.add(executor.submit(new FactorialTask(line, calculator)));
    }

    executor.shutdown();

    List<String> results = new ArrayList<>();
    for (Future<String> future : futures) {
      try {
        results.add(future.get());
      } catch (InterruptedException | ExecutionException e) {
        results.add("Calculation error: " + e.getMessage());
      }
    }

    try {
      if (!executor.awaitTermination(1, TimeUnit.MINUTES)) {
        executor.shutdownNow();
      }
    } catch (InterruptedException e) {
      executor.shutdownNow();
    }

    return results;
  }
}
