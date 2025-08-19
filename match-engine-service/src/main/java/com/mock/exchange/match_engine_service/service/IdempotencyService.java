package com.mock.exchange.match_engine_service.service;

import org.springframework.beans.factory.annotation.Autowired;

// IdempotencyService: track processed IDs in Redis
public class IdempotencyService {
private static final String KEY = "trade:processed:ids";
    @Autowired private StringRedisTemplate redis;

    /**
     * Returns true if id is new (not processed); false if duplicate.
     * Uses Redis Set add semantics: returns 1 if added, 0 if exists.
     */
    public boolean markIfNew(String eventId, Duration ttl) {
        Long added = redis.opsForSet().add(KEY, eventId);
        if (added != null && added == 1) {
            // Set TTL only once per batch (optional); or store each id with expire separately
            redis.expire(KEY, ttl);
            return true;
        }
        return false;
    }
}
