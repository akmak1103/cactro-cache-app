package org.cactro.data.cacheapi.controller;

import org.cactro.data.cacheapi.exception.CacheException;
import org.cactro.data.cacheapi.model.CacheEntry;
import org.cactro.data.cacheapi.service.RedisCacheService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cache")
public class CacheController {
    private final RedisCacheService cacheService;

    public CacheController(RedisCacheService cacheService) {
        this.cacheService = cacheService;
    }

    @PostMapping
    public ResponseEntity<String> addToCache(@RequestBody CacheEntry entry) {
        try {
            cacheService.put(entry.getKey(), entry.getValue());
            return ResponseEntity.ok("Entry added successfully");
        } catch (CacheException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{key}")
    public ResponseEntity<String> getFromCache(@PathVariable String key) {
        try {
            String value = cacheService.get(key);
            return ResponseEntity.ok(value);
        } catch (CacheException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{key}")
    public ResponseEntity<String> removeFromCache(@PathVariable String key) {
        try {
            cacheService.delete(key);
            return ResponseEntity.ok("Entry deleted successfully");
        } catch (CacheException e) {
            return ResponseEntity.notFound().build();
        }
    }
}