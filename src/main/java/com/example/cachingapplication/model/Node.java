package com.example.cachingapplication.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Node <K, V> {

    private K key;
    private V value;
    private Node<K, V> next;
    private Node<K, V> prev;

    private int frequency = 1;

    public Node(K key, V value, Node<K, V> next, Node<K, V> prev) {
        this.key = key;
        this.value = value;
        this.next = next;
        this.prev = prev;
    }

}
