package com.company;
import com.company.entities.Order;
import com.company.entities.Product;
import com.company.entities.Shop;
import com.company.entities.Storage;
import javafx.util.Pair;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.company.utils.Utils.*;

public class Main {

    public static void main(String[] args) {

        var startTime = System.currentTimeMillis()/1000.0;

        Shop shop = new Shop(10);

        shop.startThreads();

        var endTime = System.currentTimeMillis()/1000.0;

        System.out.println(endTime - startTime);

    }
}
