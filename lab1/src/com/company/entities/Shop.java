package com.company.entities;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import static com.company.utils.Utils.generateOrder;
import static com.company.utils.Utils.generateProducts;

public class Shop{
    private List<Order> orders;
    private Storage storage ;
    private int noThreads;

    public Shop(int noThreads) {

        this.noThreads = noThreads;

        HashMap<Product,Integer> products = generateProducts(3000);
        HashMap<Integer,Integer> changedProds = new HashMap<>();
        products.forEach((key, value) -> changedProds.put(key.getId(), value));
        this.storage =  new Storage(changedProds);
        this.orders = generateOrder(storage,noThreads);

    }

    public void startThreads() {

        Thread[] threads = new Thread[noThreads];

        for(int i = 0 ; i< noThreads; i++){
            threads[i] = new Thread(orders.get(i));
            threads[i].start();
        }

        for (Thread thread : threads){
            try {
                thread.join();
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
        }
    }


    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    public Storage getStorage() {
        return storage;
    }

    public void setStorage(Storage storage) {
        this.storage = storage;
    }
}
