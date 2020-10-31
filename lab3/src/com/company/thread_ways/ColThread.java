package com.company.thread_ways;
import static com.company.ThreadOps.*;
import com.company.Matrix;
import com.company.ThreadOps;
import com.company.Utils;

public class ColThread implements Runnable {


    private int firstIndex,lastIndex;
    private String name;

    public ColThread(int initFirst, int initSecond, String name) {
        firstIndex = initFirst;
        lastIndex = initSecond;
        this.name = name;
    }


    @Override
    public void run() {

        Matrix m1 = ThreadOps.m1;
        Matrix m2 = ThreadOps.m2;
        int size = resultCol.getSize();
        synchronized (resultCol) {

            for (int i = firstIndex; i <= lastIndex; i++) {

                //(size*(i%size)+i/size) % size) and (size*(i%size)+i/size) / size)
                //are used to transpose the element count to j and i
                //example: i=12 => (9*(12%9)+(12/9)=28 the element mirrored as position on column

                var resultElement = Utils.computeElement(m1.getRow((size*(i%size)+i/size) / size),
                        m2.getCol((size*(i%size)+i/size) % size));
                resultCol.setElement((size*(i%size)+i/size) / size, ((size*(i%size)+i/size) % size), resultElement);

            }
            System.out.println("\npartial result of " + name);
            resultCol.getElements().forEach(System.out::println);

        }
    }
}
