package dev.codersite.rateLimit.controller;

import dev.codersite.rateLimit.model.Quote;
import dev.codersite.rateLimit.service.QuoteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/v1")
public class QuoteController {

  private static final Logger logger = LoggerFactory.getLogger(QuoteController.class);

  @Autowired
  private QuoteService quoteService;

  @GetMapping(value = "/quotes/random", produces = MediaType.APPLICATION_JSON_VALUE)
  public Quote getQuote(HttpServletRequest httpServletRequest) throws Exception {

    return quoteService.getRandomQuote();
  }
}
