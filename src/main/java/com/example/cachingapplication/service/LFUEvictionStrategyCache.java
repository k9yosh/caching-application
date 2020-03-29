package com.example.cachingapplication.service;

import com.example.cachingapplication.model.Node;

import java.util.HashMap;
import java.util.Map;

public class LFUEvictionStrategyCache<K, V> implements Cache <K, V> {

    private Node<K, V> tail; // dummy tail to keep track of the least used and newest
    private Node<K, V> head; // dummy head to keep track of the most used

    private Map<K, Node<K, V>> container; // holds the actual cached items

    private int maxSize;
    private int currentSize;

    public LFUEvictionStrategyCache(int maxSize) {
        this.maxSize = maxSize;
        this.currentSize = 0;
        this.tail = new Node<K, V>();
        this.head = this.tail;
        this.container = new HashMap<K, Node<K, V>>();
    }

    @Override
    public V get(K key) {

        Node<K, V> node = this.container.get(key);

        if (node != null) {

            // up the frequency hit by 1
            node.setFrequency(node.getFrequency()+1);

            // If the requested node is marked as current head, but do nothing to the list
            if (node.getKey() == head.getKey()) {
                return head.getValue();
            }

            // keep switching requested node with it's prev node until the it comes to it's correct position
            while (node.getPrev() != null && node.getPrev().getFrequency() < node.getFrequency()) {

                Node<K, V> nextNode = node.getNext();
                Node<K, V> prevNode = node.getPrev();

                // If the requested node is marked as tail, then take node's prev and mark next as null since it's going to be the new tail. Then mark it as tail.
                if (node.getKey() == tail.getKey()) {
                    node.setNext(prevNode);
                    node.setPrev(prevNode.getPrev());

                    // Must set prev node's prev's next before previous node's prev is set to requested note
                    prevNode.getPrev().setNext(node);

                    prevNode.setNext(null);
                    prevNode.setPrev(node);
                    tail = prevNode;
                }
                // update links and make the switch with prev
                else {
                    node.setNext(prevNode);
                    node.setPrev(prevNode.getPrev());
                    prevNode.setNext(nextNode);
                    prevNode.setPrev(node);
                    nextNode.setPrev(prevNode);
                }

                // if requested node's prev is null that means it has made a switch with head and requested node should be the new head.
                if (node.getPrev() == null)
                    head = node;

            }

            return node.getValue();

        } else
            return null;

    }

    @Override
    public void put(K key, V value) {

        // If this key already exists then update it's value and call get() so it will take it's correct position in the list
        if (this.container.containsKey(key)) {
            Node<K, V> node = this.container.get(key);
            node.setValue(value);
            get(key);
        } else {

            Node<K, V> node = new Node<>(key, value, null, null);

            // Since the new node will be inserted at the tail of the list,
            // if the cache has reached it's maxSize remove the current tail from cache, get it's prev and make it as new node's prev and new node as current tail's prev's next.
            if (maxSize == currentSize) {
                container.remove(tail.getKey());
                node.setPrev(tail.getPrev());
                tail.getPrev().setNext(node);
            }
            // If the new node is the first node being inserted then mark it is also as the head, set current tail as new node's prev and new node as current tails next.
            else if (currentSize < maxSize) {
                if (currentSize == 0)
                    head = node;

                node.setPrev(tail);
                tail.setNext(node);
                currentSize++;
            }

            // replace current tail with new node and add the node to cache.
            container.put(key, node);
            tail = node;

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
