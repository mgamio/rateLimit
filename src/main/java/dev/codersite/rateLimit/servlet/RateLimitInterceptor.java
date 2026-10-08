package dev.codersite.rateLimit.servlet;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.ConsumptionProbe;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Component
public class RateLimitInterceptor implements HandlerInterceptor {

  private static final long CAPACITY = 10;
  private static final Duration REFILL_PERIOD = Duration.ofMinutes(1);

  // One bucket per client IP. Buckets of clients that stay idle for 10 minutes are removed.
  private final Cache<String, Bucket> buckets = Caffeine.newBuilder()
      .expireAfterAccess(Duration.ofMinutes(10))
      .maximumSize(100_000)
      .build();

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
    String clientIp = request.getRemoteAddr();
    Bucket bucket = buckets.get(clientIp, ip -> newBucket());

    ConsumptionProbe probe = bucket.tryConsumeAndReturnRemaining(1);
    if (probe.isConsumed()) {
      response.addHeader("X-Rate-Limit-Remaining", Long.toString(probe.getRemainingTokens()));
      return true;
    }

    long waitMillis = TimeUnit.NANOSECONDS.toMillis(probe.getNanosToWaitForRefill());
    response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value()); // 429
    response.addHeader("Retry-After", Long.toString((waitMillis + 999) / 1000)); // seconds, rounded up
    response.addHeader("X-Rate-Limit-Retry-After-Milliseconds", Long.toString(waitMillis));
    return false;
  }

  private Bucket newBucket() {
    return Bucket.builder()
        .addLimit(limit -> limit.capacity(CAPACITY).refillIntervally(CAPACITY, REFILL_PERIOD))
        .build();
  }
}
