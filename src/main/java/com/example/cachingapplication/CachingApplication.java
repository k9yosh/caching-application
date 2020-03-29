package com.example.cachingapplication;

import com.example.cachingapplication.factory.CacheFactory;
import com.example.cachingapplication.service.Cache;
import com.example.cachingapplication.service.LRUEvictionStrategyCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;

@SpringBootApplication
public class CachingApplication {

    @Value("${cache.maxSize}")
    private int maxSize;
    @Value("${cache.evictionStrategy}")
    private String evictionStrategy;

    private static int sMaxSize;
    private static String sEvictionStrategy;

    @PostConstruct
    public void init() {
        sMaxSize = maxSize;
        sEvictionStrategy = evictionStrategy;
    }

    public static void main(String[] args) {
        SpringApplication.run(CachingApplication.class, args);

        System.out.println("Initialized a " + sMaxSize + " max capacity cache with eviction strategy : " + sEvictionStrategy);

        Cache<Integer, String> cache = CacheFactory.getCache(sMaxSize, sEvictionStrategy);

        cache.put(1, "Deer");
        cache.get(1);
        cache.get(2);
        cache.put(2, "Rabbit");
        cache.get(1);
        cache.get(2);

        cache.put(3, "Lion");
        cache.put(4, "Monkey");
        cache.get(4);
        cache.put(3, "Lion");

    }

}
