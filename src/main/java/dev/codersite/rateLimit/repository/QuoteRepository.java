package dev.codersite.rateLimit.repository;

import dev.codersite.rateLimit.model.Quote;

import java.util.Map;

public interface QuoteRepository {
  public Map<Integer, Quote> getAllQuotes() throws Exception;
}
