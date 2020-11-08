package com.company;

import static com.company.Utils.generateRandomMatrix;



public class Main {

    public static void main(String[] args) throws InterruptedException {

        var startTime = System.currentTimeMillis()/1000.0;

        var matrix1 = generateRandomMatrix(9);
        var matrix2 = generateRandomMatrix(9);

        System.out.println("matrix 1");
        matrix1.getElements().forEach(System.out::println);
        System.out.println("\nmatrix 2");
        matrix2.getElements().forEach(System.out::println);

        ThreadOps thrds = new ThreadOps(9,matrix1,matrix2);

        //simple thread runs

        //thrds.runThreadRows();
        //thrds.runThreadColumns();
        //thrds.runThreadK();

        //threadpool runs

        //thrds.runPoolRows();
        //thrds.runPoolCols();
        thrds.runPoolK();

        var endTime = System.currentTimeMillis()/1000.0;

        System.out.println(endTime - startTime);
    }
}
