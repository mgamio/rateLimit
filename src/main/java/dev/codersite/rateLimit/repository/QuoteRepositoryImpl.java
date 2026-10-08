package dev.codersite.rateLimit.repository;

import dev.codersite.rateLimit.model.Quote;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Repository;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Repository
public class QuoteRepositoryImpl implements QuoteRepository {

  private final List<Quote> quotes;

  public QuoteRepositoryImpl() {
    this.quotes = loadQuotes("quotes.txt");
  }

  @Override
  public List<Quote> findAll() {
    return quotes;
  }

  // Each line of the file is "message;author"
  private static List<Quote> loadQuotes(String fileName) {
    ClassPathResource resource = new ClassPathResource(fileName);
    try (BufferedReader reader = new BufferedReader(
        new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))) {
      return reader.lines()
          .filter(line -> !line.isBlank())
          .map(line -> line.split(";", 2))
          .map(parts -> new Quote(parts[0].trim(), parts[1].trim()))
          .toList();
    } catch (IOException e) {
      throw new UncheckedIOException("Cannot read " + fileName, e);
    }
  }
}
