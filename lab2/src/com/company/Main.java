package com.company;

import com.company.entities.Consumer;
import com.company.entities.Producer;

import java.util.ArrayList;
import java.util.List;

import static com.company.utils.Utils.generateProducts;

public class Main {

    public static ArrayList<Integer> list = new ArrayList<Integer>();
    public static int sum = 0;

    public static void main(String[] args) {

        ArrayList<Integer> v1 = generateProducts(4);
        ArrayList<Integer> v2 = generateProducts(4);

        System.out.println(v1);
        System.out.println(v2);


        Thread producer = new Thread(new Producer(v1,v2));
        Thread consumer = new Thread(new Consumer(list));
        producer.start();
        consumer.start();




    }
}
