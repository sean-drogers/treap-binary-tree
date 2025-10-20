# Treap Binary Tree

**Language:** Java  
**Purpose:** Implements a Treap data structure — a combination of a binary search tree (BST) and a heap — supporting insertion, deletion, rotation, and search operations.

---

##  How It Works
- Each node contains a key and a random priority.  
- The tree maintains **BST order** by key and **heap order** by priority.  
- New nodes are inserted and rotated (`rotateLeft`, `rotateRight`) to maintain heap property.  
- Includes `find`, `add`, and `delete` operations with recursion.  
- Generic implementation with `Treap<E extends Comparable<E>>`.  
- Includes a JUnit test suite verifying correctness.

---

##  Build & Run
```bash
javac src/Treap.java src/TreapTest.java
java src/TreapTest
