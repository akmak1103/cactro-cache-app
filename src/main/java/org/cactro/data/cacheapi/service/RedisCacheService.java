package org.cactro.data.cacheapi.service;

import org.cactro.data.cacheapi.exception.CacheException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisCacheService {
    private final RedisTemplate<String, String> redisTemplate;
    private final int maxSize;

    public RedisCacheService(RedisTemplate<String, String> redisTemplate,
                             @Value("${cache.max.size}") int maxSize) {
        this.redisTemplate = redisTemplate;
        this.maxSize = maxSize;
    }

    public void put(String key, String value) {
        int size = redisTemplate.keys("*").size();
        if (size >= maxSize && !Boolean.TRUE.equals(redisTemplate.hasKey(key))) {
            throw new CacheException("Cache has reached maximum capacity");
        }
        redisTemplate.opsForValue().set(key, value);
    }

    public String get(String key) {
        String value = redisTemplate.opsForValue().get(key);
        if (value == null) {
            throw new CacheException("Key not found in cache: " + key);
        }
        return value;
    }

    public void delete(String key) {
        Boolean deleted = redisTemplate.delete(key);
        if (Boolean.FALSE.equals(deleted)) {
            throw new CacheException("Key not found in cache: " + key);
        }
    }
}