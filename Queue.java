package huffman;

public class Queue<T> {
    private Node<T> back;
    private int size;

    private class Node<T> {
        T data;
        Node<T> next;

        public Node(T d, Node<T> n) {
            data = d;
            next = n;
        }
    }

    public boolean isEmpty() { return size == 0; }
    public int size() { return size; }

    public void enqueue(T item) {
        size++; 
        
        if (size == 1) {
            back = new Node<>(item, null);
            back.next = back;
            return;
        }
        
        back.next = new Node<>(item, back.next);
        back = back.next;
    }

    public T dequeue() {
        T item = back.next.data;
        size--;

        if (size == 0) {
            back = null;
            return item;
        }

        back.next = back.next.next;
        return item;
    }
    
    public T peek() {
        return back.next.data;
    }
}
