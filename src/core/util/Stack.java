package util;

public class Stack<V> {

    private Node top;
    private Node bottom;
    private int size;

    public void add(V value){

        Node newNode = new Node();
        newNode.value = value;
        newNode.next = top;

        if (top == null) bottom = newNode;
        top = newNode;
        size++;

    }

    public void add(Stack<V> stack){

        stack.bottom.next = top;
        top = stack.top;

        size += stack.size;

    }

    public V pop(){

        Node oldNode = top;
        top = top.next;
        if (top == null) bottom = null;

        size--;
        return oldNode.value;

    }

    public int size() {
        return size;
    }

    public boolean isEmpty(){
        return size == 0;
    }

    private class Node {
        public Node next;
        public V value;
    }
}
