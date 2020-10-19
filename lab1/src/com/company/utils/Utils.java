package com.company.utils;

import com.company.entities.Bill;
import com.company.entities.Order;
import com.company.entities.Product;
import com.company.entities.Storage;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public final class Utils {

    public static HashMap<Product,Integer> generateProducts(int numberOfProducts) {

        HashMap<Product,Integer> result = new HashMap<>();
        Random rand = new Random();
        for (int i = 0; i < numberOfProducts; i++) {
            var product = new Product(i, i + rand.nextInt(10));
            var quantity = rand.nextInt(10) + 1;
            var both = new Pair<Product,Integer>(product,quantity);
            result.put(product,quantity);
        }
        return result;
    }

    public static List<Order> generateOrder(Storage storage, int numberOfOrders) {

        List<Order> orders = new ArrayList<>();
        Random rand = new Random();
        for(int i = 0; i < numberOfOrders ; i++) {
            var products = generateProducts(rand.nextInt(10000));
            //System.out.println(products );
            var newOrder = new Order(products,storage);
            orders.add(newOrder);
        }
        return orders;
    }

    public static int calculateExpected(HashMap<Product,Integer> products) {
        return products.keySet()
                .stream()
                .map(e -> (e.getPrice()*products.get(e)))
                .reduce(0 , Integer::sum);
    }

    public static boolean checkConsistency(Storage storage) {

        int totalFromBills = storage.getBills().stream().map(Bill::getPaidPrice).reduce(0,Integer::sum);

        return storage.getExpectedSold() == totalFromBills;
    }

}
