package com.company.utils;

import com.company.entities.MulThread;
import com.company.entities.Polynomial;
import java.util.ArrayList;
import java.util.concurrent.*;
import java.util.stream.IntStream;

public final class Operations {

    public static void simpleMultiplication(Polynomial a, Polynomial b) {

        var startTime = System.currentTimeMillis()/1000.0;

        ArrayList<Integer> resultCoefficients = new ArrayList<>();

        Polynomial result;

        for (int i = 0; i < a.getDegree()*2-1 ; i++){
            resultCoefficients.add(i,0);
        }

        for (int i = 0; i < a.getCoefficients().size(); i++) {
            // Multiply the current term of first polynomial
            // with every term of second polynomial.
            for (int j = 0; j < b.getCoefficients().size(); j++) {
                //if (a.getCoefficients().get(i) != 0 && b.getCoefficients().get(j) != 0)
                    resultCoefficients.set(i + j,  resultCoefficients.get(i+j) + a.getCoefficients().get(i) * b.getCoefficients().get(j));
            }
        }

        result = new Polynomial(resultCoefficients);
        System.out.println("The result of polinomial simple " + a.toString() + " multiplied with " + b.toString() + "is: ");
        System.out.println(result.toString());

        var endTime = System.currentTimeMillis()/1000.0;

        System.out.println(endTime - startTime);
    }

    public static void simpleMulParallel(Polynomial a, Polynomial b, int noThreads) throws InterruptedException {

        var startTime = System.currentTimeMillis()/1000.0;

        ArrayList<Integer> resultCoefficients = startThreadsSimple(a, b, noThreads);

        var result = new Polynomial(resultCoefficients);
        System.out.println("The result of polinomial parallel " + a.toString() + " multiplied with " + b.toString() + "is: ");
        System.out.println(result.toString());

        var endTime = System.currentTimeMillis()/1000.0;

        System.out.println(endTime - startTime);


    }

    private static ArrayList<Integer> startThreadsSimple(Polynomial a, Polynomial b, int noThreads) throws InterruptedException {

        var coeffPerThread = b.getDegree()   / noThreads;
        var rest = b.getDegree() % noThreads;

        var startPos = 0;
        var stopPos = 0;
        ArrayList<Integer> result = new ArrayList<>();
        Thread mulThreads[] = new Thread[noThreads];

        for (int i = 0; i < a.getDegree()*2-1 ; i++){
            result.add(i,0);
        }

        for (int i = 0; i < noThreads; i++) {

            stopPos = startPos + coeffPerThread;
            if (rest != 0) {
                stopPos++;
                rest--;
            }

            mulThreads[i] = new Thread(new MulThread(a, b, result, startPos, stopPos));
            mulThreads[i].start();
            startPos = stopPos;
        }

        for (int i = 0; i < noThreads; i++) {
            mulThreads[i].join();
        }

        return result;

    }

    public static void startKaratusbaSimple(Polynomial a, Polynomial b){

        var startTime = System.currentTimeMillis()/1000.0;

        System.out.println(a.getCoefficients());
        System.out.println(b.getCoefficients());

        var result = karatsubaSequential(a.getCoefficients(),b.getCoefficients());

        var polyResult = new Polynomial(result);
        System.out.println("The result of sequential karatusba polinomial " + a.toString() + " multiplied with " + b.toString() + "is: ");
        System.out.println(polyResult.toString());

        var endTime = System.currentTimeMillis()/1000.0;

        System.out.println(endTime - startTime);

    }

    public static ArrayList<Integer> karatsubaSequential(ArrayList<Integer> A, ArrayList<Integer> B) {
        ArrayList<Integer> product = new ArrayList<Integer>();

        IntStream.range(0,2*B.size()).forEach(e -> {
            product.add(e,0);
        });

        if (B.size() == 1) {
            product.set(product.size()-1, A.get(0) * B.get(0));
//            System.out.println("product");
//            System.out.println(product);
            return product;
        }

        int halfSize = A.size() / 2;

        //half arrays
        var aLow = new ArrayList<Integer>();
        var aHigh = new ArrayList<Integer>();
        var bLow = new ArrayList<Integer>();
        var bHigh = new ArrayList<Integer>();
        var aLowHigh = new ArrayList<Integer>();
        var bLowHigh = new ArrayList<Integer>();

        IntStream.range(0,halfSize).forEach(e -> {
            aLow.add(e,0);
            aHigh.add(e,0);
            bLow.add(e,0);
            bHigh.add(e,0);
            aLowHigh.add(e,0);
            bLowHigh.add(e,0);

        });


        //Fill low and high arrays
        for (int i = 0; i < halfSize; i++) {
            aLow.set(i, A.get(i));
            aHigh.set(i, A.get(halfSize + i));
            aLowHigh.set(i, aHigh.get(i) + aLow.get(i));

            bLow.set(i, B.get(i));
            bHigh.set(i, B.get(halfSize + i));
            bLowHigh.set(i, bHigh.get(i) + bLow.get(i));
        }

        var productLow = karatsubaSequential(aLow, bLow);
        var productHigh = karatsubaSequential(aHigh, bHigh);
        var productLowHigh = karatsubaSequential(aLowHigh, bLowHigh);

        //Construct middle portion of the product
        var productMiddle = new ArrayList<Integer>();

        IntStream.range(0,A.size()).forEach(e -> {
            productMiddle.add(e,0);
        });

        for (int i = 0; i < A.size(); ++i) {
            productMiddle.set(i, productLowHigh.get(i) - productLow.get(i) - productHigh.get(i));
        }

        //Assemble the product from the low, middle and high parts
        System.out.println("-----");
        System.out.println(productLow);
        System.out.println(productMiddle);
        System.out.println(productHigh);
        int midOffset = A.size() / 2 ;
//        System.out.println(A.size());
        for (int i = 0; i < A.size(); i++) {
            product.set(i, product.get(i) + productLow.get(i));
            product.set(i + A.size(), product.get(i + A.size()) + productHigh.get(i));
            product.set(i + midOffset , product.get(i + midOffset) + productMiddle.get(i));
            //System.out.println(i + product.toString());
        }

        // + 1*x^6  + 2*x^4  + 2*x^3  + 1*x^2  + 2*x^1  + 1*x^0

        //System.out.println(product);

        return product;
    }

    public static void startKaratusbaParallel(Polynomial a, Polynomial b) throws ExecutionException, InterruptedException {

        var startTime = System.currentTimeMillis()/1000.0;

        System.out.println(a.getCoefficients());
        System.out.println(b.getCoefficients());

        var result = karatsubaParallel(a.getCoefficients(),b.getCoefficients());

        var polyResult = new Polynomial(result);
        System.out.println("The result of parallel karatusba polinomial " + a.toString() + " multiplied with " + b.toString() + "is: ");
        System.out.println(polyResult.toString());

        var endTime = System.currentTimeMillis()/1000.0;

        System.out.println(endTime - startTime);

    }

    public static ArrayList karatsubaParallel(ArrayList<Integer> A, ArrayList<Integer> B) throws ExecutionException, InterruptedException {

        ArrayList<Integer> product = new ArrayList<Integer>();

        IntStream.range(0,2*B.size()).forEach(e -> {
            product.add(e,0);
        });

        if (B.size() == 1) {
            product.set(product.size()-1, A.get(0) * B.get(0));

            return product;
        }

        int halfSize = A.size() / 2;

        //half arrays
        var aLow = new ArrayList<Integer>();
        var aHigh = new ArrayList<Integer>();
        var bLow = new ArrayList<Integer>();
        var bHigh = new ArrayList<Integer>();
        var aLowHigh = new ArrayList<Integer>();
        var bLowHigh = new ArrayList<Integer>();

        IntStream.range(0,halfSize).forEach(e -> {
            aLow.add(e,0);
            aHigh.add(e,0);
            bLow.add(e,0);
            bHigh.add(e,0);
            aLowHigh.add(e,0);
            bLowHigh.add(e,0);

        });

        //Fill low and high arrays
        for (int i = 0; i < halfSize; i++) {
            aLow.set(i, A.get(i));
            aHigh.set(i, A.get(halfSize + i));
            aLowHigh.set(i, aHigh.get(i) + aLow.get(i));

            bLow.set(i, B.get(i));
            bHigh.set(i, B.get(halfSize + i));
            bLowHigh.set(i, bHigh.get(i) + bLow.get(i));
        }

        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newCachedThreadPool();
        Callable<ArrayList<Integer>> task1 = () -> karatsubaSequential(aLow, bLow);
        Callable<ArrayList<Integer>> task2 = () -> karatsubaSequential(aHigh, bHigh);
        Callable<ArrayList<Integer>> task3 = () -> karatsubaSequential(aLowHigh, bLowHigh);

        Future<ArrayList<Integer>> futureProductLow = executor.submit(task1);
        Future<ArrayList<Integer>> futureProductHigh = executor.submit(task2);
        Future<ArrayList<Integer>> futureProductLowHigh = executor.submit(task3);

        var productLow = futureProductLow.get();
        var productHigh = futureProductHigh.get();
        var productLowHigh = futureProductLowHigh.get();

        executor.shutdown();

        //Construct middle portion of the product
        var productMiddle = new ArrayList<Integer>();

        IntStream.range(0,A.size()).forEach(e -> {
            productMiddle.add(e,0);
        });

        for (int i = 0; i < A.size(); ++i) {
            productMiddle.set(i, productLowHigh.get(i) - productLow.get(i) - productHigh.get(i));
        }

        //Assemble the product from the low, middle and high parts
        System.out.println("-----");
        System.out.println(productLow);
        System.out.println(productMiddle);
        System.out.println(productHigh);
        int midOffset = A.size() / 2 ;
        for (int i = 0; i < A.size(); i++) {
            product.set(i, product.get(i) + productLow.get(i));
            product.set(i + A.size(), product.get(i + A.size()) + productHigh.get(i));
            product.set(i + midOffset , product.get(i + midOffset) + productMiddle.get(i));

        }

        return product;


    }



}
