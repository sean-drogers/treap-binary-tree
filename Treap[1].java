package HW4;
//I pledge my honor that I have abided by the Stevens Honor System.
import java.util.Random;
import java.util.Stack;

/**
 * A Treap is a randomized balanced binary search tree.
 * It maintains both the binary search tree property (using keys)
 * and the heap property (using priorities).
 *
 * @param <E> The type of keys stored in the treap, which must be Comparable.
 */
public class Treap<E extends Comparable<E>> {

    private Random priorityGenerator;
    private Node<E> root;

    private static class Node<E> {
        public E data;
        public int priority;
        public Node<E> left;
        public Node<E> right;

        public Node(E key, int priority){
            if(key == null){
                throw new NullPointerException();
            }
            this.data = key;
            this.priority = priority;
            left = null;
            right = null;
        }

        public Node<E> rotateRight(){
            Node<E> temp = this.left;
            this.left = temp.right;
            temp.right = this;

            return temp;
        }

        public Node<E> rotateLeft(){
            Node<E> temp = this.right;
            this.right = temp.left;
            temp.left = this;

            return temp;
        }

        @Override
        public String toString() {
            return "(key = "+data+", priority="+priority+")";
        }
    }

    public Treap(){
        priorityGenerator = new Random();
    }

    public Treap(long seed){
        priorityGenerator = new Random(seed);
    }

    /**
     * The method adds a key to the treap with unique priority
     *
     * @param key is the key to be added
     * @return true if key was inserted, return false if it already exist
     * @throws NullPointerException if key is null
     */
    public boolean add(E key){
        int priority = priorityGenerator.nextInt();
        if(key == null){
            throw new NullPointerException();
        }

        while(!add(key,priority)){
            if(keyExists(root,key)){
                return false;
            }
            priority = priorityGenerator.nextInt();
        }
        return true;
    }

    private boolean keyExists(Node<E> root, E key){
        while(root!=null){
            int compare = key.compareTo(root.data);
            if(compare == 0){
                return true;
            }
            if (compare < 0) {
                root = root.left;
            } else {
                root = root.right;
            }
        }
        return false;
    }

    /**
     * Adds a key with the given priority
     *
     * @param key the key to be added
     * @param priority the priority of the node
     * @return true if key was added, false if already exists
     * @throws NullPointerException if key is null
     */
    public boolean add(E key, int priority){
        if(key == null){
            throw new NullPointerException();
        }

        if(priorityExists(root,priority)){
            return false;
        }

        Node<E> newNode = new Node<>(key,priority);

        if(root == null){
            root = newNode;
            return true;
        }

        Stack<Node<E>> stack = new Stack<>();
        Node<E> current = root;

        while(true){
            stack.push(current);
            int compare = key.compareTo(current.data);

            if(compare == 0){ // there is a duplicate
                return false;
            } else if(compare < 0) {
                if(current.left == null){
                    current.left = newNode;
                    stack.push(newNode);
                    break;
                }
                current = current.left;
            } else {
                if(current.right == null){
                    current.right = newNode;
                    stack.push(newNode);
                    break;
                }
                current = current.right;
            }
        }

        reheap(stack);

        return true;
    }

    private boolean priorityExists(Node<E> root, int p){
        if(root == null){
            return false;
        }

        java.util.Stack<Node<E>> stack = new java.util.Stack<>();
        stack.push(root);

        while(!stack.isEmpty()){
            Node<E> currentNode = stack.pop();

            if(currentNode.priority == p){
                return true;
            }

            if(currentNode.left != null){
                stack.push(currentNode.left);
            }

            if(currentNode.right != null){
                stack.push(currentNode.right);
            }
        }

        return false;
    }

    private void reheap(java.util.Stack<Node<E>> stack){
        Node<E> child = stack.pop();

        while(!stack.isEmpty()) {
            Node<E> parent = stack.pop();

            if (child.priority <= parent.priority) {
                return;
            }
            Node<E> sub;
            if (parent.left == child) {
                sub = parent.rotateRight();
            } else {
                sub = parent.rotateLeft();
            }

            if (stack.isEmpty()) {
                root = sub;
            } else {
                Node<E> grandParent = stack.peek();
                if(grandParent.left == parent){
                    grandParent.left = sub;
                } else {
                    grandParent.right = sub;
                }
            }
            child = sub;
        }
    }

    /**
     * Deletes a key from the treap
     *
     * @param key the key to be deleted
     * @return true if can be found and deleted, false if not
     * @throws NullPointerException if key is null
     */
    public boolean delete(E key){
        if(key == null){
            throw new NullPointerException();
        }

        Node<E> parent = null;
        Node<E> current = root;
        while(current != null && !key.equals(current.data)){
            parent = current;
            if(key.compareTo(current.data) < 0){
                current = current.left;
            } else {
                current = current.right;
            }
        }
        if(current == null){
            return false;
        }

        while(current.left != null || current.right != null){
            Node<E> sub;
            if(current.left == null){
                sub = current.rotateLeft();
            } else if (current.right == null){
                sub = current.rotateRight();
            } else if(current.left.priority > current.right.priority){
                sub = current.rotateRight();
            } else {
                sub = current.rotateLeft();
            }

            if(parent == null){
                root = sub;
            } else if(parent.left == current){
                parent.left = sub;
            } else {
                parent.right = sub;
            }
            parent = sub;
        }

        if(parent == null){
            root = null;
        } else if(parent.left == current){
            parent.left = null;
        } else {
            parent.right = null;
        }
        return true;

    }

    private boolean find(Node<E> root, E key){
        if(root == null || key == null){
            throw new NullPointerException();
        }
        Node<E> current = root;
        int compare = key.compareTo(current.data);
            if(compare == 0){
                return true;
        }
            if(compare < 0) {
                return find(root.left, key);
            } else {
                return find(root.right, key);
            }
    }

    /**
     * Checks weather the treap has the key
     *
     * @param key the key to be found
     * @return true if key is found, false if not
     * @throws NullPointerException if key is null
     */

    public boolean find(E key){
        if(key == null){
            throw new NullPointerException();
        }
        return find(root, key);
    }

    /**
     * Prints out the Treap in preorder
     *
     * @return the treap in preorder
     */

    public String toString(){
        String result = "";
        java.util.Stack<Node<E>> nodes = new java.util.Stack<Node<E>>();
        java.util.Stack<Integer> depths = new java.util.Stack<Integer>();
        nodes.push(root);
        depths.push(0);

        while(!nodes.isEmpty()){
            Node<E> current = nodes.pop();
            int depth = depths.pop();

            for(int i = 0; i < 2 * depth; i++){
                result += " ";
            }

            if(current == null){
                result += "null\n";
            } else {
                result += current.toString() + "\n";
                nodes.push(current.right);
                depths.push(depth+1);
                nodes.push(current.left);
                depths.push(depth+1);
            }
        }
        return result;
    }
}
