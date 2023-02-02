package dev.codersite.rateLimit.controller;

import dev.codersite.rateLimit.model.Quote;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.awt.*;

@RestController
@RequestMapping("/v1")
public class QuoteController {

  private static final Logger logger = LoggerFactory.getLogger(QuoteController.class);

  @GetMapping(value = "/quotes/random", produces = MediaType.APPLICATION_JSON_VALUE)
  public Quote getTechQuote(HttpServletRequest httpServletRequest) throws Exception {

    Quote quote = new Quote();
    quote.setAuthor("author");
    quote.setMessage("quote is ...");
    return quote;
  }
}
