package com.sachin.lrucache.basic;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

class LRUCache {
    private final int capacity;
    private final Map<Integer, Node> map;
    private final Map<Integer, Node> map2 = new LinkedHashMap<>();
    private final Node head, tail;

    public LRUCache(int capacity) {
        this.capacity = capacity;

        map = new HashMap<>();

        // Dummy head and tail (insert/removal)
        head = new Node(0, 0);
        tail = new Node(0, 0);
        head.next = tail;
        tail.prev = head;
    }

    /* If the key already exists in the cache:
     *   - Get the node using the key.
     *   - Remove the node from the doubly linked list i.e., from the LRU.
     *   - Insert it at the head of the doubly linked list (i.e., mark as Most Recently Used).
     *   - Return the value for the given key.
     *
     * If the key is not present return -1.
     */
    public int get(int key) {
        if(map.containsKey(key)) {
            Node node = map.get(key);

            // remove the node
            remove(node);

            // insert to head
            insertToHead(node);

            return node.value;
        }
        return -1;
    }

    private void remove(Node node) {
        node.prev.next = node.next;
        node.next.prev = node.prev;
    }

    private void insertToHead(Node node) {
        node.next = head.next;
        node.prev = head;

        node.next.prev = node;
        head.next = node;
    }

    /* If the key already exists in the cache:
     *   - Remove the data from the doubly linked list.
     *   - It will be added again with the new value as fresh MRU.
     *   - This handles the update scenario
     *
     * Create a new node with the given key and value.
     * Insert it at the head of the doubly linked list (i.e., mark as Most Recently Used).
     * Add the node to the map for access in O(1).
     *
     * If the cache has exceeded the allowed capacity:
     *   - The just before the dummy tail is the Least Recently Used (lru).
     *   - Remove this node from the doubly linked list.
     *   - Remove its key from the map.
     */
    public void put(int key, int value) {
        if(map.containsKey(key)) {
            remove(map.get(key));
        }

        Node node = new Node(key, value);
        insertToHead(node);
        map.put(key, node);

        if(map.size() > capacity) {
            Node lru = tail.prev;
            remove(lru);
            map.remove(lru.key);
        }
    }

    public void print() {
        if(head.next == null || head.next == tail) {
            return;
        }

        System.out.print("[");
        Node ptr = head.next;
        while(ptr != tail) {
            System.out.print(ptr.value + " ");
            ptr = ptr.next;
        }
        System.out.println("]");
    }
}
