import java.util.ArrayList;

public class MinHeap<T> {

    private ArrayList<HeapNode<T>> heap;

    public MinHeap(int initialSize){
        heap = new ArrayList<>(initialSize);
    }

    public MinHeap(){
        this(10);
    }

    public void add(T object, double priority){
        add(new HeapNode<T>(object, priority));
    }

    public void add(HeapNode<T> object){
        heap.add(object);
        bubbleUp(heap.size() - 1);
    }

    public HeapNode<T> popRoot(){

        swap(0, heap.size() - 1);
        HeapNode<T> temp = heap.getLast();
        heap.removeLast();

        int curNode = 0;
        int childA;
        int childB;

        while (true){

            childA = 2 * curNode + 1;
            childB = 2 * curNode + 2;

            if (childA >= heap.size()){
                return temp;
            }
            if (childB >= heap.size()){
                if(heap.get(curNode).priority > heap.get(childA).priority) swap(curNode, childA);
                return temp;
            }

            if (heap.get(childB).priority < heap.get(childA).priority){
                childA = childB;
            }
            if (heap.get(curNode).priority <= heap.get(childA).priority){
                return temp;
            }

            swap(curNode, childA);
            curNode = childA;

        }

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
        HeapNode<T> temp = heap.get(a);
        heap.set(a, heap.get(b));
        heap.set(b, temp);
    }

}
