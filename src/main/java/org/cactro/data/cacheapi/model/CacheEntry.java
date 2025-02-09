package org.cactro.data.cacheapi.model;

import lombok.Data;
import lombok.AllArgsConstructor;

@Data
@AllArgsConstructor
public class CacheEntry {
    private String key;
    private String value;
}