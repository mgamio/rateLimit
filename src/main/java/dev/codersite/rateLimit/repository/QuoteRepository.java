package dev.codersite.rateLimit.repository;

import dev.codersite.rateLimit.model.Quote;

import java.util.List;

public interface QuoteRepository {
  List<Quote> findAll();
}
