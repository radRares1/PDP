package com.company.thread_ways;
import com.company.Matrix;
import com.company.ThreadOps;
import com.company.Utils;
import static com.company.ThreadOps.*;

public class KThread implements Runnable {

    private int taskNumber,noTasks;

    public KThread(int initTaskNumber,int initNoTasks){
        this.noTasks = initNoTasks;
        this.taskNumber = initTaskNumber;
    }

    @Override
    public void run() {

        synchronized (resultK) {
            int size = resultK.getSize();
            for (int i = taskNumber; i < size*size; i += noTasks) {
                var resultElement = Utils.computeElement(m1.getRow(i / size), m2.getCol(i % size));
                resultK.setElement(i / size, i % size, resultElement);
            }

            System.out.println("\npartial result of task" + taskNumber);
            resultK.getElements().forEach(System.out::println);
        }
    }
}
