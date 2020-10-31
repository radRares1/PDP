package com.company;

import static com.company.Utils.generateRandomMatrix;



public class Main {

    public static void main(String[] args) throws InterruptedException {

        var matrix1 = generateRandomMatrix(9);
        var matrix2 = generateRandomMatrix(9);

        System.out.println("matrix 1");
        matrix1.getElements().forEach(System.out::println);
        System.out.println("\nmatrix 2");
        matrix2.getElements().forEach(System.out::println);

        ThreadOps thrds = new ThreadOps(4,matrix1,matrix2);

        //thrds.runThreadColumns();
        //thrds.runThreadRows();
        thrds.runThreadK();

    }
}
