package dev.codersite.rateLimit.servlet;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.ConsumptionProbe;
import io.github.bucket4j.Refill;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.Duration;
import java.util.Enumeration;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Component
public class RateLimitInterceptor implements HandlerInterceptor {
  private static final Logger logger = LoggerFactory.getLogger(RateLimitInterceptor.class);

  private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();
  private final long tokensToConsumeByDefault = 1;
  private long capacity = 10;
  private long tokens = 10;

  private final Bucket defaultBucket = Bucket.builder()
      .addLimit(Bandwidth.classic(capacity, Refill.intervally(tokens, Duration.ofMinutes(1))))
      .build();

  public RateLimitInterceptor() {}

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                           Object handler) throws Exception {
    String clientIP = getClientIP(request);
    logger.info(clientIP);

    Bucket bucketByClientIP;
    if (buckets.containsKey(clientIP)) {
      bucketByClientIP = buckets.get(clientIP);
    } else {
      bucketByClientIP = this.defaultBucket;
      buckets.put(clientIP, bucketByClientIP);
    }

    ConsumptionProbe probe = bucketByClientIP.tryConsumeAndReturnRemaining(this.tokensToConsumeByDefault);
    if (probe.isConsumed()) {
      response.addHeader("X-Rate-Limit-Remaining",
          Long.toString(probe.getRemainingTokens()));
      return true;
    }

    response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value()); // 429
    response.addHeader("X-Rate-Limit-Retry-After-Milliseconds",
        Long.toString(TimeUnit.NANOSECONDS.toMillis(probe.getNanosToWaitForRefill())));

    return false;
  }

  private String getClientIP(HttpServletRequest request) {
    String ip = request.getHeader("X-FORWARDED-FOR");

    if (ip == null || ip.isEmpty()) {
      ip = request.getRemoteAddr();
    }

    return ip;
  }

}
