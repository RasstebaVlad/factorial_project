package com.rassteba.factorial_project;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.math.BigInteger;
import java.util.concurrent.Callable;

@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FactorialTask implements Callable<String> {

  String line;
  FactorialCalculator calculator;

  @Override
  public String call() {
    try {
      int num = Integer.parseInt(line.trim());
      BigInteger result = calculator.calculate(num);
      return num + " = " + result;
    } catch (NumberFormatException e) {
      return "Invalid number format: " + line;
    } catch (IllegalArgumentException e) {
      return "Error: " + e.getMessage();
    }
  }
}
