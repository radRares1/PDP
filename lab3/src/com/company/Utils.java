package com.company;

import java.util.ArrayList;
import java.util.Random;
import java.util.stream.IntStream;

public final class Utils {

    public static ArrayList<Integer> generateRow(int sizeOfVector) {

        ArrayList<Integer> result = new ArrayList<>();
        Random rand = new Random();
        IntStream.range(0,sizeOfVector).forEach(e -> result.add(rand.nextInt(10) + 1));
        return result;
    }

    public static Matrix generateRandomMatrix(int size){

        var newMatrix = new Matrix(size);
        var elementsHolder = new ArrayList<ArrayList<Integer>>(size);

        IntStream.range(0,size).forEach(e -> elementsHolder.add(generateRow(size)));
        newMatrix.setElements(elementsHolder);
        return newMatrix;

    }

    public static int computeElement(ArrayList<Integer> row, ArrayList<Integer> col){
       return IntStream.range(0, row.size()).map(e -> row.get(e) * col.get(e)).sum();
    }
}