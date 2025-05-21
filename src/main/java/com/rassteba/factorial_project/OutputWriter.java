package com.rassteba.factorial_project;

import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@Component
public class OutputWriter {
  public void writeResults(List<String> results, String filePath) throws IOException {
    try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(filePath))) {
      for (String result : results) {
        writer.write(result);
        writer.newLine();
      }
    }
  }
}
