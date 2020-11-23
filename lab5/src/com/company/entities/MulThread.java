package com.company.entities;

import java.util.ArrayList;

public class MulThread implements Runnable {

    private Polynomial a, b;
    private ArrayList<Integer> result;
    private int startPos, stopPos;

    public MulThread(Polynomial a, Polynomial b, ArrayList<Integer> result, int startPos, int stopPos) {
        this.a = a;
        this.b = b;
        this.result = result;
        this.startPos = startPos;
        this.stopPos = stopPos;
    }


    @Override
    public void run() {

        synchronized (result) {

            for (int i = startPos; i < stopPos; i++) {
                for (int j = 0; j < a.getDegree(); j++) {
                    //if (a.getCoefficients().get(i) != 0 && b.getCoefficients().get(j) != 0)
                        result.set(i + j, result.get(i +j) + a.getCoefficients().get(i) * b.getCoefficients().get(j));
                }
            }
            System.out.println(result);
        }

    }
}
