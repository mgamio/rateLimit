package dev.codersite.rateLimit.service;

import dev.codersite.rateLimit.model.Quote;

public interface QuoteService {
  public Quote getRandomQuote() throws Exception;
}
