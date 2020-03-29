package com.example.cachingapplication.service;

import com.example.cachingapplication.model.Node;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>This is a cache implementation based on {@link com.example.cachingapplication.service.Cache Cache} Interface.</p>
 * <p>This implementation uses the Least Recently Used (LRU) as the eviction strategy</p>
 *
 * @param <K> Data type of the Key
 * @param <V> Data type of the Value
 *
 * @author yomal
 * @version 1.0
 * @since 1.0
 */
public class LRUEvictionStrategyCache<K, V> implements Cache <K, V> {

    private Node<K, V> tail; // dummy tail to keep track of the LRU
    private Node<K, V> head; // dummy head to keep track of the MRU

    private Map<K, Node<K, V>> container; // holds the actual cached items

    private int maxSize;
    private int currentSize;

    public LRUEvictionStrategyCache(int maxSize) {
        this.maxSize = maxSize;
        this.currentSize = 0;
        this.tail = new Node<K, V>();
        this.head = this.tail;
        this.container = new HashMap<K, Node<K, V>>();
    }

    @Override
    public V get(K key) {

        Node<K, V> node = this.container.get(key);

        if (node == null)
            return null;
        // If the requested node is marked as current head, then do nothing to the list.
        else if (node.getKey() == head.getKey())
            return head.getValue();

        Node<K, V> nextNode = node.getNext();
        Node<K, V> prevNode = node.getPrev();

        // If the requested node is marked as tail, then take node's prev and mark next as null since it's going to be the new tail. Then mark it as tail.
        if (node.getKey() == tail.getKey()) {
            prevNode.setNext(null);
            tail = prevNode;
        }
        // If the requested node is in between head and tail, remove links to the requested node and and update with new ones (set requested node's next node's prev as prev node and prev node's next as next node) since it will be moved to head.
        else if (node.getKey() != head.getKey()) {
            nextNode.setPrev(prevNode);
            prevNode.setNext(nextNode);
        }

        // Attach and update links between current head and requested node since it will be marked as the new head. Make the current head as requested node's next and requested node as current head's prev, then mark requested node as current head.
        node.setNext(head);
        head.setPrev(node);

        // Mark requested node as head and it's prev as null since the head has no prev.
        head = node;
        head.setPrev(null);

        return node.getValue();
    }

    @Override
    public void put(K key, V value) {

        if (!this.container.containsKey(key)) {

            // Since new node will be inserted at the head of the list, make the current head as new node's next and new node as current head's prev. Then replace current head with new node.
            Node<K, V> node = new Node<>(key, value, head, null);
            head.setPrev(node);
            container.put(key, node);
            head = node;

            // If the cache size reaches the max limit, then get the tail and remove it from the cache. Mark the tail's prev as current tail and next as null since it is at the tail.
            if (maxSize == currentSize) {
                container.remove(tail.getKey());
                tail = tail.getPrev();
                tail.setNext(null);
            }
            // If the new node is the first node being inserted then mark it also as the tail.
            else if (currentSize < maxSize) {
                if (currentSize == 0)
                    tail = node;

                currentSize++;
            }

        }

    }

    @Override
    public void resetCache() {
        this.currentSize = 0;
        this.tail = new Node<K, V>();
        this.head = this.tail;
        this.container = new HashMap<K, Node<K, V>>();
    }

    @Override
    public int getCurrentCapacity() {
        return this.currentSize;
    }

}
