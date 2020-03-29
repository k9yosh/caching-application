package com.example.cachingapplication;

import com.example.cachingapplication.service.Cache;
import com.example.cachingapplication.service.LFUEvictionStrategyCache;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
public class LFUEvictionStrategyCacheTests {

    private Cache<Integer, String> cache;

    public LFUEvictionStrategyCacheTests(@Value( "${cache.maxSize}") int maxSize) {
        this.cache = new LFUEvictionStrategyCache<>(maxSize);
    }

    @Test
    public void cacheWillReturnNullIfNotFoundTest() {
        cache = new LFUEvictionStrategyCache<>(3);
        cache.put(1, "Lion");
        assertNull(cache.get(2), "Must be null");
    }

    @Test
    public void cacheAtMaxCapacityRemovesLeastUsedToAddNewItemTest() {
        this.cache = new LFUEvictionStrategyCache<>(3);

        cache.put(1, "Lion");
        cache.put(2, "Goat");
        cache.put(3, "Bear");

        cache.get(1);
        cache.get(3);

        assertEquals(cache.get(2), "Goat", "Must return Goat");

        cache.put(4, "Rabbit");

        assertNull(cache.get(2), "Must return null");
        assertEquals(cache.get(4), "Rabbit", "Must return Rabbit");
    }

    @Test
    public void cacheResetClearsAllItems() {
        this.cache = new LFUEvictionStrategyCache<>(3);

        cache.put(1, "Lion");
        cache.put(2, "Goat");
        cache.put(3, "Bear");

        assertEquals(cache.get(1), "Lion", "Must return Lion");
        assertEquals(cache.get(2), "Goat", "Must return Goat");
        assertEquals(cache.get(3), "Bear", "Must return Bear");

        cache.resetCache();

        assertNull(cache.get(1), "Must return null");
        assertNull(cache.get(2), "Must return null");
        assertNull(cache.get(3), "Must return null");
    }

    @Test
    public void cacheMustNotExceedMaxSize() {
        this.cache = new LFUEvictionStrategyCache<>(3);

        cache.put(1, "Lion");
        cache.put(2, "Goat");
        cache.put(3, "Bear");
        cache.put(4, "Rabbit");

        assertEquals(cache.getCurrentCapacity(), 3, "Must be equal to 3");
    }

}
