package dev.codersite.rateLimit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class RateLimitApplicationTests {

	@Autowired
	private WebApplicationContext context;

	private MockMvc mockMvc;

	@BeforeEach
	void setUp() {
		mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
	}

	private static MockHttpServletRequestBuilder randomQuoteFrom(String ip) {
		return get("/v1/quotes/random").with(request -> {
			request.setRemoteAddr(ip);
			return request;
		});
	}

	@Test
	void returnsAQuote() throws Exception {
		mockMvc.perform(randomQuoteFrom("10.0.0.1"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.message").isNotEmpty())
				.andExpect(jsonPath("$.author").isNotEmpty())
				.andExpect(header().string("X-Rate-Limit-Remaining", "9"));
	}

	@Test
	void rejectsTheEleventhRequestWithinAMinute() throws Exception {
		for (int i = 1; i <= 10; i++) {
			mockMvc.perform(randomQuoteFrom("10.0.0.2")).andExpect(status().isOk());
		}
		mockMvc.perform(randomQuoteFrom("10.0.0.2"))
				.andExpect(status().isTooManyRequests())
				.andExpect(header().exists("Retry-After"))
				.andExpect(header().exists("X-Rate-Limit-Retry-After-Milliseconds"));
	}

	@Test
	void eachClientHasItsOwnLimit() throws Exception {
		for (int i = 1; i <= 10; i++) {
			mockMvc.perform(randomQuoteFrom("10.0.0.3")).andExpect(status().isOk());
		}
		mockMvc.perform(randomQuoteFrom("10.0.0.3")).andExpect(status().isTooManyRequests());

		mockMvc.perform(randomQuoteFrom("10.0.0.4")).andExpect(status().isOk());
	}

	@Test
	void aFakeForwardedForHeaderDoesNotBypassTheLimit() throws Exception {
		for (int i = 1; i <= 10; i++) {
			mockMvc.perform(randomQuoteFrom("10.0.0.5").header("X-Forwarded-For", "1.2.3." + i))
					.andExpect(status().isOk());
		}
		mockMvc.perform(randomQuoteFrom("10.0.0.5").header("X-Forwarded-For", "9.9.9.9"))
				.andExpect(status().isTooManyRequests());
	}
}
