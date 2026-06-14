package core.util;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.LinkedList;

public class NumericTreeMap<V, K extends Number> {

    private Node root;
    private final Node nil = new Node(null, null);

    private int size;

    public NumericTreeMap(){

        nil.isRed = false;
        nil.childLeft = null;
        nil.childRight = null;

        root = nil;
        size = 0;

    }

    public V put(V value, K key, boolean replace){

        Node newNode = new Node(value,key);
        Node parent = null;
        Node curNode = root;

        while(curNode != nil) {

            parent = curNode;

            if (key.doubleValue() < curNode.key.doubleValue()){

                curNode = curNode.childLeft;

            } else if (key.doubleValue() > curNode.key.doubleValue()){

                curNode = curNode.childRight;

            } else {

                V temp = curNode.value;
                if (replace) curNode.value = value;
                else throw new InputMismatchException("The key: \"" + key + "\" is already assigned to a value.");
                return temp;

            }

        }

        if (parent == null){
            root = newNode;
        } else if (newNode.key.doubleValue() < parent.key.doubleValue()) {
            parent.setChildLeft(newNode);
        } else {
            parent.setChildRight(newNode);
        }

        correctInsertion(newNode);

        size++;
        return null;

    }

    public V put(V value, K key){
        return put(value, key, true);
    }

    private void correctInsertion(Node target){

        while (target.parent != null && target.parent.isRed) {

            Node p = target.parent;
            Node g = p.parent;
            Node u;

            if (p == g.childLeft) {

                u = g.childRight;
                if (u.isRed) {

                    p.isRed = false;
                    u.isRed = false;
                    g.isRed = true;
                    target = g;

                } else {

                    if (target == p.childRight) {
                        target = p;
                        target.rotateLeft();
                    }
                    target.parent.isRed = false;
                    target.parent.parent.isRed = true;
                    target.parent.parent.rotateRight();
                    break;

                }

            } else {

                u = g.childLeft;
                if (u.isRed) {

                    p.isRed = false;
                    u.isRed = false;
                    g.isRed = true;
                    target = g;

                } else {

                    if (target == p.childLeft) {
                        target = p;
                        target.rotateRight();
                    }
                    target.parent.isRed = false;
                    target.parent.parent.isRed = true;
                    target.parent.parent.rotateLeft();
                    break;

                }


            }

        }

        root.isRed = false;
    }

    private Node getNode(K key){

        Node curNode = root;

        while (curNode != nil){

            if (key.doubleValue() < curNode.key.doubleValue()){

                curNode = curNode.childLeft;

            } else if (key.doubleValue() > curNode.key.doubleValue()) {

                curNode = curNode.childRight;

            } else {

                return curNode;

            }

        }

        throw new InputMismatchException("No Value with key: \"" + key.toString() + "\".");

    }

    public V get(K key){
        return getNode(key).value;
    }

    public V remove(K key){

        Node target = getNode(key);
        V temp = target.value;
        boolean hasLeft = target.childLeft != nil;
        boolean hasRight = target.childRight != nil;
        Node newNode;

        if (target.childLeft == nil){

            newNode = target.childRight;

        } else if (target.childRight == nil) {

            newNode = target.childLeft;

        } else {

            newNode = target.childRight;
            while (newNode.childLeft != nil){
                newNode = newNode.childLeft;
            }

            if (newNode.parent.childLeft == newNode) {
                newNode.parent.setChildLeft(newNode.childRight);
            } else {
                newNode.parent.setChildRight(newNode.childRight);
            }

            newNode.setChildLeft(target.childLeft);
            newNode.setChildRight(target.childRight);

        }

        if (target == root) {
            root = newNode;
            newNode.parent = null;
        } else if (target.parent.childLeft == target){
            target.parent.setChildLeft(newNode);
        } else {
            target.parent.setChildRight(newNode);
        }

        correctDeletion(newNode);

        size--;
        return temp;

    }

    private void correctDeletion(Node target){

        Node s;
        Node p;

        while (!target.isRed && target != root){

            p = target.parent;

            if (p.childLeft == target){
                s = p.childRight;

                if(s.isRed){
                    s.isRed = false;
                    p.isRed = true;
                    p.rotateLeft();
                } else if (!(s.childLeft.isRed || s.childRight.isRed)) {
                    s.isRed = true;
                    target = p;
                } else {
                    if (!s.childRight.isRed) {
                        s.childLeft.isRed = false;
                        s.isRed = true;
                        s.rotateRight();
                        s = p.childRight;
                    }

                    s.isRed = p.isRed;
                    p.isRed = false;
                    s.childRight.isRed = false;
                    p.rotateLeft();

                    break;
                }

            } else {
                s = p.childLeft;

                if(s.isRed){
                    s.isRed = false;
                    p.isRed = true;
                    p.rotateRight();
                } if (!(s.childLeft.isRed || s.childRight.isRed)) {
                    s.isRed = true;
                    target = p;
                } else {
                    if (!s.childLeft.isRed) {
                        s.childRight.isRed = false;
                        s.isRed = true;
                        s.rotateLeft();
                        s = p.childLeft;
                    }

                    s.isRed = p.isRed;
                    p.isRed = false;
                    s.childLeft.isRed = false;
                    p.rotateRight();

                    break;
                }
            }
        }

        root.isRed = false;

    }

    public int getHeight(){

        if (root == nil){
            return 0;
        }

        LinkedList<Node> queue = new LinkedList<>();
        Node curNode;

        queue.add(root);
        int i = 0;

        while (!queue.isEmpty()){

            i++;
            int levelLength = queue.size();

            for (int j = 0; j < levelLength; j++){

                curNode = queue.pop();
                if (curNode.childLeft != nil) queue.add(curNode.childLeft);
                if (curNode.childRight != nil) queue.add(curNode.childRight);

            }
        }

        return i;

    }

    public ArrayList<V> toArrayList(){

        if (root == nil) return new ArrayList<V>(0);

        LinkedList<Node> queue = new LinkedList<>();
        Node curNode;

        queue.add(root);
        ArrayList<V> ary = new ArrayList<>(size);

        while (!queue.isEmpty()){

            int levelLength = queue.size();

            for (int j = 0; j < levelLength; j++){

                curNode = queue.pop();
                ary.add(curNode.value);

                if (curNode.childLeft != nil) queue.add(curNode.childLeft);
                if (curNode.childRight != nil) queue.add(curNode.childRight);

            }
        }

        return ary;

    }

    public int size() {
        return size;
    }

    private class Node {

        public Node parent;
        public Node childLeft = nil;
        public Node childRight = nil;

        public V value;
        public K key;

        public boolean isRed = true;

        public Node(V value, K key){
            this.value = value;
            this.key = key;
        }


        public void setChildLeft(Node newChild){
            newChild.parent = this;
            childLeft = newChild;
        }

        public void setChildRight(Node newChild){
            newChild.parent = this;
            childRight = newChild;
        }


        public void rotateRight(){

            Node b = childLeft;

            b.parent = parent;
            setChildLeft(b.childRight);
            b.setChildRight(this);

            if(b.parent == null){
                root = b;
            } else {
                if (b.parent.childLeft == this){
                    b.parent.childLeft = b;
                } else {
                    b.parent.childRight = b;
                }
            }

        }

        public void rotateLeft(){

            Node b = childRight;

            b.parent = parent;
            setChildRight(b.childLeft);
            b.setChildLeft(this);

            if(b.parent == null){
                root = b;
            } else {
                if (b.parent.childLeft == this){
                    b.parent.childLeft = b;
                } else {
                    b.parent.childRight = b;
                }
            }
        }

    }

}
