package com.mock.exchange.auth_service.rate_limit;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

/**
 * This service implements a sliding window rate limiter using Redis lists.
 */
@Service
@RequiredArgsConstructor
public class RateLimiterService {
private final RedisTemplate<String, String> redisTemplate;

    private final int MAX_REQUESTS = 5;
    private final long WINDOW_SECONDS = 60;
     /**
     * Checks whether a client (identified by IP or user key) is rate-limited.
     * @param key unique client key (e.g., IP address)
     * @return true if allowed, false if rate limited
     */
    public boolean isAllowed(String key) {
        long now = Instant.now().getEpochSecond();
        String redisKey = "login:limit:" + key;

        // Add current timestamp to list
        redisTemplate.opsForList().rightPush(redisKey, String.valueOf(now));

        // Trim to latest N requests
        redisTemplate.opsForList().trim(redisKey, -MAX_REQUESTS, -1);

        // Read recent timestamps
        List<String> timestamps = redisTemplate.opsForList().range(redisKey, 0, -1);

        if (timestamps.size() < MAX_REQUESTS) {
            return true; // under limit
        }

        long first = Long.parseLong(timestamps.get(0));
        return now - first >= WINDOW_SECONDS;
    }
}
