package com.rassteba.factorial_project;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@Component
public class InputReader {
  public List<String> readLines(String filePath) throws IOException {
    return Files.readAllLines(Paths.get(filePath));
  }
}
