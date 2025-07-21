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

