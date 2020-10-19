package com.company.entities;

import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Producer implements Runnable {

    private ReentrantLock mutex;
    private boolean hasProduced;
    private ArrayList<Integer> vectorOne,vectorTwo;
    private ArrayList<Integer> result;

    public Producer(ArrayList<Integer> v1,ArrayList<Integer>v2){
        mutex = new ReentrantLock();
        hasProduced = false;
        vectorOne = v1;
        vectorTwo = v2;
        result = new ArrayList<>();
    }

    @Override
    public void run() {

        mutex.lock();
        //for simplicity we assume that the vectors have the same length
        IntStream.range(0,vectorOne.size()).forEach(e -> result.add(vectorOne.get(e)*vectorTwo.get(e)));
        hasProduced = true;
        mutex.unlock();

    }

    public void setVectorOne(ArrayList<Integer> vectorOne) {
        this.vectorOne = vectorOne;
    }

    public void setVectorTwo(ArrayList<Integer> vectorTwo) {
        this.vectorTwo = vectorTwo;
    }

    public boolean hasProduced() {
        return hasProduced;
    }

    public ArrayList<Integer> getResult() {
        return result;
    }
}
