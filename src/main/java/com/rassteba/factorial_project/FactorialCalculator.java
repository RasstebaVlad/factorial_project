package com.rassteba.factorial_project;

import org.springframework.stereotype.Component;

import java.math.BigInteger;

@Component
public class FactorialCalculator {
  public BigInteger calculate(int n) {
    if (n < 0) {
      throw new IllegalArgumentException("The number cannot be negative");
    }
    BigInteger result = BigInteger.ONE;
    for (int i = 2; i <= n; i++) {
      result = result.multiply(BigInteger.valueOf(i));
    }
    return result;
  }
}
