package dev.codersite.rateLimit.controller;

import dev.codersite.rateLimit.model.Quote;
import dev.codersite.rateLimit.service.QuoteService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1")
public class QuoteController {

  private final QuoteService quoteService;

  public QuoteController(QuoteService quoteService) {
    this.quoteService = quoteService;
  }

  @GetMapping(value = "/quotes/random", produces = MediaType.APPLICATION_JSON_VALUE)
  public Quote getQuote() {
    return quoteService.getRandomQuote();
  }
}
