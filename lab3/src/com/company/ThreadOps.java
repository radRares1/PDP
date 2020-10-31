package com.company;

import com.company.thread_ways.ColThread;
import com.company.thread_ways.KThread;
import com.company.thread_ways.RowThread;

public final class ThreadOps {

    private int noThreads;
    public static Matrix m1,m2,resultRow,resultCol,resultK;

    public ThreadOps(int initThreads, Matrix initM1, Matrix initM2){
        noThreads=initThreads;
        m1=initM1;
        m2=initM2;
        resultRow = new Matrix(9);
        resultCol = new Matrix(9);
        resultK = new Matrix(9);
    }

    public void runThreadRows() throws InterruptedException {

        Thread t1 = new Thread(new RowThread(0,19,"1"));
        Thread t2 = new Thread(new RowThread(20,39,"2"));
        Thread t3 = new Thread(new RowThread(40,59,"3"));
        Thread t4 = new Thread(new RowThread(60,80,"4"));

        t1.start();
        t2.start();
        t3.start();
        t4.start();

        t1.join();
        t2.join();
        t3.join();
        t4.join();

        System.out.println("\n row result");
        resultRow.getElements().forEach(System.out::println);

    }

    public void runThreadColumns() throws InterruptedException {

        Thread t1 = new Thread(new ColThread(0,20,"1"));
        Thread t2 = new Thread(new ColThread(20,40,"2"));
        Thread t3 = new Thread(new ColThread(40,60,"3"));
        Thread t4 = new Thread(new ColThread(60,80,"4"));

        t1.start();
        t2.start();
        t3.start();
        t4.start();

        t1.join();
        t2.join();
        t3.join();
        t4.join();

        System.out.println("\n col result");
        resultCol.getElements().forEach(System.out::println);

    }

    public void runThreadK() throws InterruptedException {

        Thread t1 = new Thread(new KThread(0,4));
        Thread t2 = new Thread(new KThread(1,4));
        Thread t3 = new Thread(new KThread(2,4));
        Thread t4 = new Thread(new KThread(3,4));

        t1.start();
        t2.start();
        t3.start();
        t4.start();

        t1.join();
        t2.join();
        t3.join();
        t4.join();

        System.out.println("\n k result");
        resultK.getElements().forEach(System.out::println);

    }




}
