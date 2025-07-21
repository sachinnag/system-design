## What is an LRU Cache?
LRU (Least Recently Used) Cache is a data structure that stores a fixed number of items. When the capacity is reached and a new item is added, it evicts the least recently used item.

## LRU Cache Requirements
### Operations (all in O(1) time):
- `get(key)`: return the value if present, else -1. Also, mark it as most recently used.
- `put(key, value)`: add the key-value pair. If full, remove the least recently used item.

## Design Choices
### To achieve O(1) time:
- Use a **HashMap** for quick key access.
- Use a **Doubly Linked List** to maintain access order (recently used to least used).

### Structure:
```lua
+--------------+             +------------------+       +----------------+
|   HashMap    |  ------->   |  Doubly Linked   | <-->  |     Node       |
|<key, Node>   |             |     List         |       | (key, value)   |
+--------------+             +------------------+       +----------------+
```

## UML Class Diagram
```pgsql
+------------------+
|     LRUCache     |
+------------------+
| - capacity: int  |
| - map: Map       |
| - head: Node     |
| - tail: Node     |
+------------------+
| + get(int): int  |
| + put(int, int): void |
+------------------+

+------------------+
|      Node        |
+------------------+
| - key: int       |
| - value: int     |
| - prev: Node     |
| - next: Node     |
+------------------+
```

|**Note**: Understanding why dummy head and tail nodes are used in a doubly linked list is key to writing clean and bug-free code — especially in an LRU Cache.

## How dummy head and tail simplify list management?
### The Problem Without Dummy Nodes
When you use a regular doubly linked list (without dummy head and tail), you have to write extra logic every time you:

- Insert at the head 
- Remove the tail 
- Remove a node (especially the first or last)

Because you need to check for:
- Is the list empty? 
- Is the node at the head or tail? 
- Are we inserting/removing the only node?

This leads to a lot of if-else conditions and increases bug chances.

### What Are Dummy Head and Tail Nodes?
Dummy nodes are sentinel nodes at the beginning and end of the doubly linked list.
```markdown
head <-> A <-> B <-> C <-> tail
```

Here:
- head is not a real data node — it’s just a placeholder before the first element. 
- tail is also a placeholder after the last element.

### Benefits of Dummy Head/Tail
#### 1. Unified Insert Logic
   You always insert after head, so you never have to check if the list is empty or if you're inserting at the beginning.

```java
insertToHead(node):
node.prev = head
node.next = head.next
head.next.prev = node
head.next = node
```

No need to check:
- If head.next == null 
- If node == tail

#### 2. Simplified Deletion
Every node has guaranteed prev and next, so deletion becomes:

```java
remove(node):
node.prev.next = node.next
node.next.prev = node.prev
```

No checks for:
- node == head 
- node == tail 
- node == null

#### 3. Avoids Edge Cases
- No special-case logic for 1-node list 
- No risk of NullPointerException for first/last nodes
 
### With vs Without Dummy Nodes
| Operation      | Without Dummy Nodes | With Dummy Nodes    |
| -------------- | ------------------- | ------------------- |
| Insert at head | Check if head null  | Constant logic      |
| Remove tail    | Special case        | Always `tail.prev`  |
| Remove middle  | Must check ends     | Uniform logic       |
| Empty check    | `head == null`      | `head.next == tail` |


### Interview Insight
Using dummy nodes shows you understand:
- Defensive design 
- Clean code 
- Reducing conditional branching


### What is Thread-Safety in LRU Cache?
Thread-safety ensures that multiple threads accessing or modifying the cache don’t corrupt the internal state (e.g., doubly linked list, map).

For example, these must be atomic:
- get(key) → Read and potentially update access order 
- put(key, value) → Insert, evict, and rewire nodes

### Common Issues in Multi-threaded LRU Cache
- **Race condition:** Two threads accessing and modifying the linked list at the same time. 
- **Visibility problem:** One thread's changes aren't visible to another due to CPU caching. 
- **Deadlock** (if multiple locks are poorly designed)

### Thread-Safe Design Approaches
#### 1. Synchronized Methods or Blocks (Simplest)
- Use synchronized on `get()` and `put()`

Pros: 
- Easy to implement 
- No complex concurrency APIs

Cons:
- Entire method is locked — lower concurrency 
- Only one thread can access the cache at a time

#### 2. Use `ReentrantLock` for Better Granularity
Allows you to:
- Only lock critical sections (like list updates)
- Improve **performance** vs `synchronized`

#### 3. Use `ConcurrentHashMap` + Custom Lock for List
- `ConcurrentHashMap` is thread-safe 
- You only need to lock the doubly linked list for insertion/removal

#### 4. Use `LinkedHashMap` with AccessOrder and Override
If asked to **write fast, clean, thread-safe LRU**:
```java
public class ThreadSafeLRUCache<K, V> extends LinkedHashMap<K, V> {
    private final int capacity;

    public ThreadSafeLRUCache(int capacity) {
        super(capacity, 0.75f, true); // accessOrder = true
        this.capacity = capacity;
    }

    @Override
    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        return size() > capacity;
    }

    public synchronized V get(Object key) {
        return super.get(key);
    }

    public synchronized V put(K key, V value) {
        return super.put(key, value);
    }
}
```

> **When Asked in Interviews**\
> If the question is: "How would you make your LRU cache thread-safe?"
> 
> You can say:\
> I’d add thread-safety using Java's built-in synchronization (synchronized or ReentrantLock) around access methods like get() and put(). If high performance is a concern, I’d use ConcurrentHashMap for the map and fine-grained locking for the doubly linked list. Alternatively, for production use, I might extend LinkedHashMap with accessOrder and synchronize public methods for simplicity and correctness.

