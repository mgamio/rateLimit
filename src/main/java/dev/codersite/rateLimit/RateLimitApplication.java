package dev.codersite.rateLimit;

import dev.codersite.rateLimit.model.Quote;
import dev.codersite.rateLimit.repository.QuoteRepository;
import dev.codersite.rateLimit.repository.QuoteRepositoryImpl;
import dev.codersite.rateLimit.service.QuoteService;
import dev.codersite.rateLimit.service.QuoteServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Map;

@SpringBootApplication
public class RateLimitApplication {

	public static void main(String[] args) {
		SpringApplication.run(RateLimitApplication.class, args);
	}

	@Bean
	public QuoteService quoteService() {
		return new QuoteServiceImpl();
	}

	@Bean
	public QuoteRepository quoteRepository() {
		return new QuoteRepositoryImpl();
	}

	@Bean
	public Map<Integer, Quote> mapOfQuotes() throws Exception {
		return quoteRepository().getAllQuotes();
	}
}
