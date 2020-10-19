package com.company;

import com.company.entities.Consumer;
import com.company.entities.Producer;

import java.util.ArrayList;

import static com.company.utils.Utils.generateProducts;

public class Main {

    public static void main(String[] args) {

        ArrayList<Integer> v1 = generateProducts(4);
        ArrayList<Integer> v2 = generateProducts(4);

        Producer p = new Producer(v1,v2);
        p.run();
        System.out.println(v1);
        System.out.println(v2);
        System.out.println(p.getResult());

        if(p.hasProduced()){
            Consumer c = new Consumer(p.getResult());
            c.run();
            System.out.println(c.getResult());
        }

        //System.out.println();

    }
}
