package com.example.cachingapplication.service;

/**
 * <p>This {@code Cache} interface is used to build a basic in-memory object cache</p>
 *
 * @param <K> Data type of the Key
 * @param <V> Data type of the Value
 *
 * @author yomal
 * @version 1.0
 * @since 1.0
 */
public interface Cache<K, V> {

    V get(K key);
    void put(K key, V value);
    void resetCache();

    int getCurrentCapacity();

}
