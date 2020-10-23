package com.company.utils;



import java.util.ArrayList;
import java.util.Random;
import java.util.stream.IntStream;

public final class Utils {

    public static ArrayList<Integer> generateVector(int sizeOfVector) {

        ArrayList<Integer> result = new ArrayList<>();
        Random rand = new Random();
        IntStream.range(0,sizeOfVector).forEach(e -> result.add(rand.nextInt(10) + 1));
        return result;
    }
}
