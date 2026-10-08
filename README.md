# rateLimit
A rate limit is the number of API calls an application or user can make in a given period.

Learn to implement a rate limit per client with the Bucket4j library in this [post](https://codersite.dev/rate-limit/).

![rate-limit](https://codersite.dev/assets/images/rateLimitAlgorithm.jpg)

## Requirements

Java 17 or later. The project uses Spring Boot 4.1 and Bucket4j 8.21.

## Run it

```bash
./mvnw spring-boot:run
curl -i http://localhost:8082/v1/quotes/random
```

Each client IP can send 10 requests per minute. The 11th request gets `429 Too Many Requests` with a `Retry-After` header.

## Test it

```bash
./mvnw test
```

[![ko-fi](https://ko-fi.com/img/githubbutton_sm.svg)](https://ko-fi.com/M4M4UE9UD)
