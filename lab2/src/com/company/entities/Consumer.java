package com.company.entities;

import com.company.Main;

import java.net.Inet4Address;
import java.util.ArrayList;


public class Consumer implements Runnable {

    final ArrayList<Integer> resultFromProducer;
    int sum;

    public Consumer(ArrayList<Integer> list) {
        this.resultFromProducer = Main.list;
        sum = Main.sum;
    }

    @Override
    public void run() {

        synchronized (resultFromProducer) {

            for (int i = 0; i < 4; i++) {

                if (resultFromProducer.isEmpty()) {
                    try {
                        System.out.println("consumer waiting for producer ");
                        resultFromProducer.wait();

                    } catch (InterruptedException e) {
                        System.out.println(e.getMessage());
                    }
                }
                int currentElement = resultFromProducer.remove(0);
                sum += currentElement;
                System.out.println("consume = " + currentElement);
                System.out.println("current sum is " + sum);

                resultFromProducer.notifyAll();
                try {
                    Thread.sleep(500);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
}