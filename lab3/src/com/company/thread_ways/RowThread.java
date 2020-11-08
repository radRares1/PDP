package com.company.thread_ways;
import static com.company.ThreadOps.*;
import com.company.Matrix;
import com.company.ThreadOps;
import com.company.Utils;

public class RowThread implements Runnable {


    private int firstIndex,lastIndex;
    private String name;

    public RowThread(int initFirst, int initSecond, String name) {
        firstIndex = initFirst;
        lastIndex = initSecond;
        this.name = name;
    }


    @Override
    public void run() {

        Matrix m1 = ThreadOps.m1;
        Matrix m2 = ThreadOps.m2;
        int size = resultRow.getSize();
        synchronized (resultRow) {

            //System.out.println(currentElementi);
            for (int i = firstIndex; i <= lastIndex; i++) {
                var resultElement = Utils.computeElement(m1.getRow(i / size), m2.getCol(i % size));
                resultRow.setElement(i / size, i % size, resultElement);
            }

            System.out.println("\npartial result of " + name);
            resultRow.getElements().forEach(System.out::println);

        }
    }
}
