package dev.codersite.rateLimit.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/quotes/v1")
public class QuoteController {

  private static final Logger logger = LoggerFactory.getLogger(QuoteController.class);

  @GetMapping("/tech")
  public String getTechQuote(HttpServletRequest httpServletRequest) throws Exception {

    return "quote is";
  }
}
