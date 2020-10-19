package com.company.entities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Storage {

    private HashMap<Integer, Integer> quantities;
    private List<Bill> bills = new ArrayList<>();
    private int expectedSold;

    public HashMap<Integer, Integer> getQuantities() {
        return quantities;
    }

    public void setQuantities(HashMap<Integer, Integer> quantities) {
        this.expectedSold = 0;
        this.quantities = quantities;
    }

    public List<Bill> getBills() {
        return bills;
    }

    public void setBills(List<Bill> bills) {
        this.bills = bills;
    }

    public Storage(HashMap<Integer, Integer> initQuants) {
        quantities = initQuants;
    }

    //not actually needed since we don't implement restocking
    public void addProduct(int product, int quantity) {
        if (quantities.containsKey(product)) {
            quantities.replace(product, quantities.getOrDefault(product, 0) + quantity);
        } else
            quantities.put(product, quantity);
    }

    public void sellProduct(int product, int quantity) {
        if (quantities.containsKey(product)) {
            if(quantities.get(product) <= 0)
                throw new RuntimeException("The product is unavailable");
            else if (quantities.get(product) < quantity)
                throw new RuntimeException("There is not enough quantity for your order");
            else {
                quantities.replace(product, quantities.get(product) - quantity);
            }
        }
        else
            throw new RuntimeException("The product does not exist");
    }

    public HashMap<Integer, Integer> getProducts() {
        return quantities;
    }

    public int getExpectedSold() {
        return expectedSold;
    }

    public void setExpectedSold(int expectedSold) {
        this.expectedSold = expectedSold;
    }
}
