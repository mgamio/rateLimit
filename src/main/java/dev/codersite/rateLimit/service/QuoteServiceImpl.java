package dev.codersite.rateLimit.service;

import dev.codersite.rateLimit.model.Quote;
import dev.codersite.rateLimit.repository.QuoteRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class QuoteServiceImpl implements QuoteService {

  private final QuoteRepository quoteRepository;

  public QuoteServiceImpl(QuoteRepository quoteRepository) {
    this.quoteRepository = quoteRepository;
  }

  @Override
  public Quote getRandomQuote() {
    List<Quote> quotes = quoteRepository.findAll();
    return quotes.get(ThreadLocalRandom.current().nextInt(quotes.size()));
  }
}
