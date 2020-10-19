package com.company.entities;

import javafx.util.Pair;

import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;

import static com.company.utils.Utils.checkConsistency;

public class Order implements Runnable{

    private HashMap<Product,Integer> products;
    private ReentrantLock mutex;
    private int totalPrice;
    private Storage storage;
    private Bill bill;
    private static int BID = 0;
    private boolean isDone = false;

    public boolean isDone() {
        return isDone;
    }

    public void setDone(boolean done) {
        isDone = done;
    }

    public Order(HashMap<Product,Integer> initList, Storage storage) {
        this.products = initList;
        totalPrice = 0;
        this.storage = storage;
        mutex = new ReentrantLock();
    }

    public void run() {

        mutex.lock();

        System.out.println("storage before " + storage.getProducts().toString());


        for(Product product : products.keySet()) {

            try {
                storage.sellProduct(product.getId(), products.get(product));
                System.out.println("product " + product.getId() + " has been sold with quantity" + product.getPrice());
                totalPrice += product.getPrice() * products.get(product);
                this.bill = new Bill(BID,products,totalPrice);
                List<Bill> temp = storage.getBills();
                temp.add(this.bill);
                storage.setBills(temp);
                BID += 1;
                storage.setExpectedSold(storage.getExpectedSold() + bill.getPaidPrice());
//                System.out.println("bill " + this.bill.getNeededProducts().toString());
//                System.out.println("storage " + storage.getProducts().toString());

                isDone = true;


            }
            catch (Exception e){
                System.out.println(e.getMessage());
            }

        }

        System.out.println("the consistency check is " + checkConsistency(storage));


        mutex.unlock();

    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public Bill getBill(){
        return bill;
    }
}
