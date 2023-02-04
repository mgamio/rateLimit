package dev.codersite.rateLimit.repository;

import dev.codersite.rateLimit.model.Quote;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class QuoteRepositoryImpl implements QuoteRepository {

  private static final Logger logger = LoggerFactory.getLogger(QuoteRepositoryImpl.class);

  @Override
  public Map<Integer, Quote> getAllQuotes() throws Exception {
    Map<Integer, Quote> quotes = new ConcurrentHashMap<>();
    Resource resource = new ClassPathResource("quotes.txt");
    try {
      List<String> allLines = Files.readAllLines(Paths.get(resource.getURI()));
      int n = 1;
      for (String line: allLines) {
        logger.info(line);
        String[] attributes = line.split(";");
        Quote quote = new Quote();
        quote.setMessage(attributes[0]);
        quote.setAuthor(attributes[1]);
        quotes.put(n++, quote);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    logger.info("nro quotes: " + quotes.size());
    return quotes;
  }

}
