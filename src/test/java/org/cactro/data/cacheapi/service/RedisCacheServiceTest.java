package org.cactro.data.cacheapi.service;

import org.cactro.data.cacheapi.exception.CacheException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class RedisCacheServiceTest {

    @Autowired
    private RedisCacheService cacheService;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @BeforeEach
    void setUp() {
        redisTemplate.getConnectionFactory().getConnection().flushAll();
    }

    @Test
    void whenPuttingNewEntry_thenItShouldBeStored() {
        cacheService.put("test", "value");
        assertEquals("value", cacheService.get("test"));
    }

    @Test
    void whenCacheIsFull_thenThrowException() {
        // Fill the cache
        for (int i = 0; i < 10; i++) {
            cacheService.put("key" + i, "value" + i);
        }

        // Try to add one more entry
        assertThrows(CacheException.class, () -> {
            cacheService.put("overflow", "value");
        });
    }

    @Test
    void whenDeletingExistingKey_thenItShouldBeRemoved() {
        cacheService.put("test", "value");
        cacheService.delete("test");
        assertThrows(CacheException.class, () -> cacheService.get("test"));
    }

    @Test
    void whenDeletingNonExistentKey_thenThrowException() {
        assertThrows(CacheException.class, () -> cacheService.delete("nonexistent"));
    }
}