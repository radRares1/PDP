package com.company.entities;

import javafx.util.Pair;

import java.util.HashMap;
import java.util.List;

public class Bill {
    private int BID;
    private HashMap<Product,Integer> neededProducts;
    private int paidPrice;

    public Bill(int BID, HashMap<Product, Integer> neededProducts, int paidPrice) {
        this.BID = BID;
        this.neededProducts = neededProducts;
        this.paidPrice = paidPrice;
    }

    public HashMap<Product, Integer> getNeededProducts() {
        return neededProducts;
    }

    public int getPaidPrice() {
        return paidPrice;
    }

    public int getBID() {
        return BID;
    }
}
