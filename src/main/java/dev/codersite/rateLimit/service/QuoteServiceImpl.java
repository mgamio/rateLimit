package dev.codersite.rateLimit.service;

import dev.codersite.rateLimit.model.Quote;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class QuoteServiceImpl implements QuoteService {

  @Autowired
  private Map<Integer, Quote> mapOfQuotes;

  @Override
  public Quote getRandomQuote() throws Exception {
    int n = getRandomNumber(1, mapOfQuotes.size());
    return mapOfQuotes.get(n);
  }

  private int getRandomNumber(int min, int max) {
    return ThreadLocalRandom.current().nextInt(min, max + 1);
  }

}
