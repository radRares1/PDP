package com.company.entities;

import java.util.ArrayList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Consumer extends Thread {

    private ReentrantLock mutex;
    private Condition hasConsumed;
    private ArrayList<Integer> inputFromConsumer;
    private Integer result;

    public Consumer(ArrayList<Integer> input){
        mutex = new ReentrantLock();
        hasConsumed = mutex.newCondition();
        inputFromConsumer = input;
        result = 0;
    }

    @Override
    public void run()  {

        mutex.lock();
        result = inputFromConsumer.stream().reduce(0, Integer::sum);
        hasConsumed.notify();
        mutex.unlock();

    }

    public void setInputFromConsumer(ArrayList<Integer> inputFromConsumer) {
        this.inputFromConsumer = inputFromConsumer;
    }

    public Integer getResult() {
        return result;
    }
}
