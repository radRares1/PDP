package com.company.entities;

import com.company.Main;

import java.util.ArrayList;


public class Producer implements Runnable {

    ArrayList<Integer> vector1, vector2;
    final ArrayList<Integer> result;

    public Producer(ArrayList<Integer> v1, ArrayList<Integer> v2) {
        this.vector1 = v1;
        this.vector2 = v2;
        result = Main.list;
    }

    @Override
    public void run() {
        synchronized (result) {


            for (int i = 0; i < 4; i++) {

                if (!result.isEmpty()) {
                    try {
                        System.out.println("producer waiting for consumer");
                        result.wait();

                    } catch (InterruptedException e) {
                        System.out.println(e.getMessage());
                    }
                }

                int prod = vector1.remove(0) * vector2.remove(0);
                System.out.println("produce=" + prod);
                result.add(prod);
                result.notifyAll();

                System.out.println(Main.list);

                try {
                    Thread.sleep(500);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();


                }

            }
        }
    }
}


