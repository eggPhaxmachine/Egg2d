package core.util;

import java.util.ArrayList;

public class MinHeap<T> {

    private ArrayList<Node<T>> heap;

    public MinHeap(int initialSize){
        heap = new ArrayList<>(initialSize);
    }

    public MinHeap(){
        this(10);
    }

    public void add(T object, double priority){
        heap.add(new Node<>(object, priority));
        bubbleUp(heap.size() - 1);
    }

    public T popRoot(){

        swap(0, heap.size() - 1);
        Node<T> temp = heap.getLast();
        heap.removeLast();

        int curNode = 0;
        int childA;
        int childB;

        while (true){

            childA = 2 * curNode + 1;
            childB = 2 * curNode + 2;

            if (childA >= heap.size()){
                break;
            }
            if (childB >= heap.size()){
                if(heap.get(curNode).priority > heap.get(childA).priority) swap(curNode, childA);
                break;
            }

            if (heap.get(childB).priority < heap.get(childA).priority){
                childA = childB;
            }
            if (heap.get(curNode).priority <= heap.get(childA).priority){
                break;
            }

            swap(curNode, childA);
            curNode = childA;

        }

        return temp.object;

    }

    private void bubbleUp(int i){

        int curNode = i;
        int nodeParent = (int)Math.floor(curNode/2.0);

        while(true){
            if(heap.get(curNode).priority < heap.get(nodeParent).priority){
                swap(curNode, nodeParent);
            } else {
                return;
            }

            curNode = nodeParent;
            nodeParent = (int)Math.floor(curNode/2.0);

            if (curNode == 0){
                return;
            }
        }
    }
    
    private void swap(int a, int b){
        Node<T> temp = heap.get(a);
        heap.set(a, heap.get(b));
        heap.set(b, temp);
    }

    private static class Node<T> {

        T object;
        double priority;

        public Node(T object, double priority){
            this.object = object;
            this.priority = priority;
        }

    }

}
