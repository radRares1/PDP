package com.company;

import com.company.entities.Polynomial;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import com.company.utils.Operations;

public class Main {

    public static void main(String[] args) throws InterruptedException, ExecutionException {

        var testPoli = new Polynomial(new ArrayList<Integer>(List.of(1,2,1,1,1,1,1,1,1,1,1,1,1,1,1,1)));
        var testPoli2 = new Polynomial(new ArrayList<Integer>(List.of(1,2,1,1,1,1,1,1,1,1,1,1,1,1,1,1)));

        Operations.simpleMultiplication(testPoli,testPoli2);
        Operations.simpleMulParallel(testPoli,testPoli2,3);
        Operations.startKaratusbaSimple(testPoli,testPoli2);
        Operations.startKaratusbaParallel(testPoli,testPoli2);



    }
}
