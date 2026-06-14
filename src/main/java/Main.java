import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;

public class Main {

    public static void main(String[] args){

        double t1 = 0;
        double t2 = 0;

        int iMax = 10000000;
        int jMax = 4;

        for (int i = 0; i < iMax; i++){
            long startTime;
            long endTime;

            startTime = System.nanoTime();
            ArrayList<Integer> arrayList = new ArrayList<>();
            for (int j = 0; j < jMax; j++) {
                arrayList.add(rand(j), j);
            }
            endTime = System.nanoTime();

            t1 += (endTime - startTime) / 1e9d;

            startTime = System.nanoTime();
            HashMap<Integer, Integer> treeMap = new HashMap<>(100);
            for (int j = 0; j < jMax; j++) {
                treeMap.put(rand(j),j);
            }
            endTime = System.nanoTime();

            t2 += (endTime - startTime) / 1e9d;

            System.out.println(i);
        }

        System.out.println(t1 / iMax);
        System.out.println(t2 / iMax);

        System.out.println(t2 / t1);

    }

    public static int rand(int s){
        return (int)(Math.random() * s);
    }

}
