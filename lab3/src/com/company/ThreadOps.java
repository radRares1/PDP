package com.company;

import com.company.thread_ways.ColThread;
import com.company.thread_ways.KThread;
import com.company.thread_ways.RowThread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

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

    public void runPoolRows() throws InterruptedException {

        Runnable t1 = new RowThread(0,19,"1");
        Runnable t2 = new RowThread(20,39,"2");
        Runnable t3 = new RowThread(40,59,"3");
        Runnable t4 = new RowThread(60,80,"4");



        // creates a thread pool with noumber of thredas
        // threads as the fixed pool size
        ExecutorService pool = Executors.newFixedThreadPool(2);

        // passes the RowThread objects to the pool to execute
        pool.execute(t1);
        pool.execute(t2);
        pool.execute(t3);
        pool.execute(t4);

        if(!pool.awaitTermination(500, TimeUnit.MILLISECONDS)) {
            pool.shutdownNow();

            System.out.println("\n row pool result");
            resultRow.getElements().forEach(System.out::println);
        }

    }

    public void runPoolK() throws InterruptedException {

        Runnable t1 = new KThread(0,4);
        Runnable t2 = new KThread(1,4);
        Runnable t3 = new KThread(2,4);
        Runnable t4 = new KThread(3,4);

        // creates a thread pool with noumber of thredas
        // threads as the fixed pool size
        ExecutorService pool = Executors.newFixedThreadPool(2);

        // passes the RowThread objects to the pool to execute
        pool.execute(t1);
        pool.execute(t2);
        pool.execute(t3);
        pool.execute(t4);

        if(!pool.awaitTermination(500, TimeUnit.MILLISECONDS)) {
            pool.shutdownNow();

            System.out.println("\n k pool result");
            resultK.getElements().forEach(System.out::println);
        }

    }

    public void runPoolCols() throws InterruptedException{

        Runnable t1 = new ColThread(0,19,"1");
        Runnable t2 = new ColThread(20,39,"2");
        Runnable t3 = new ColThread(40,59,"3");
        Runnable t4 = new ColThread(60,80,"4");

        // creates a thread pool with noumber of thredas
        // threads as the fixed pool size
        ExecutorService pool = Executors.newFixedThreadPool(2);

        // passes the RowThread objects to the pool to execute
        pool.execute(t1);
        pool.execute(t2);
        pool.execute(t3);
        pool.execute(t4);

        if(!pool.awaitTermination(500, TimeUnit.MILLISECONDS)) {
            pool.shutdownNow();

            System.out.println("\n col pool result");
            resultCol.getElements().forEach(System.out::println);
        }


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

        System.out.println("\n row thread result");
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
