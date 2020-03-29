package com.example.cachingapplication.factory;

import com.example.cachingapplication.service.Cache;
import com.example.cachingapplication.service.LFUEvictionStrategyCache;
import com.example.cachingapplication.service.LRUEvictionStrategyCache;

public class CacheFactory {

    public static Cache getCache(int maxSize, String evictionStrategy) {

        if (evictionStrategy != null) {
            if (evictionStrategy.equals("LRU"))
                return new LRUEvictionStrategyCache<>(maxSize);
            else if (evictionStrategy.equals("LFU"))
                return new LFUEvictionStrategyCache(maxSize);
            else
                throw new IllegalArgumentException("Unsupported Eviction Strategy");
        } else
            throw new IllegalArgumentException("Unsupported Eviction Strategy");

    }

}
