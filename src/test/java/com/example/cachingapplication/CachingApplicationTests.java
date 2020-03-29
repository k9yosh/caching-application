package com.example.cachingapplication;

import com.example.cachingapplication.service.Cache;
import com.example.cachingapplication.service.LRUEvictionStrategyCache;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
class CachingApplicationTests {

    @Test
    void contextLoads() {
    }

}
