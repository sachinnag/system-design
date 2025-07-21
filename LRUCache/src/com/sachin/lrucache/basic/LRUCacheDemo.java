package com.sachin.lrucache.basic;

public class LRUCacheDemo {
    public static void main(String[] args) {
        LRUCache cache = new LRUCache(5);

        cache.put(2, 2);
        cache.put(3,3);
        cache.put(4, 4);
        cache.put(5,5);
        cache.put(6, 6);
        cache.put(7,7);

        cache.print();

        cache.get(5);

        cache.print();
    }
}
