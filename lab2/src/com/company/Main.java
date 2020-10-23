package com.company;

import com.company.entities.Consumer;
import com.company.entities.Producer;

import java.util.ArrayList;

import static com.company.utils.Utils.generateVector;

public class Main {

    public static void main(String[] args) {

        ArrayList<Integer> v1 = generateVector(4);
        ArrayList<Integer> v2 = generateVector(4);

        Producer p = new Producer(v1,v2);
        Consumer c = new Consumer(p.getResult());

        Thread[] threads = new Thread[2];
        threads[0] = p;
        threads[1] = c;






        System.out.println(v1);
        System.out.println(v2);
        System.out.println(p.getResult());

//        if(p.hasProduced()){
//            Consumer c = new Consumer(p.getResult());
//            c.run();
//            System.out.println(c.getResult());
//        }

        //System.out.println();

    }
}
