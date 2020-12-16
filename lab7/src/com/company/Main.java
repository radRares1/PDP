package com.company;

import mpi.MPI;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class Main {

    public static void startO2(Polynomial a, Polynomial b){

        int n = MPI.COMM_WORLD.Size();
        int[] aCoef = a.getCoefficients();
        int[] bCoef = b.getCoefficients();

        var startPos = 0;
        var stopPos = 0;
        var coeffPerNode = aCoef.length/(n-1);

        for(int i = 1 ;i<n;i++)
        {
            startPos = stopPos;
            stopPos = startPos + coeffPerNode;

            if (i == n-1) {
               stopPos = aCoef.length;
            }

            int[] bufferStart = new int[1];
            int[] bufferStop = new int[1];
            bufferStart[0] = startPos;
            bufferStop[0] = stopPos;


            MPI.COMM_WORLD.Send(aCoef, 0, aCoef.length,MPI.INT,i,0);
            MPI.COMM_WORLD.Send(bCoef, 0,bCoef.length,MPI.INT,i,0);
            MPI.COMM_WORLD.Send(bufferStart, 0,1,MPI.INT,i,0);
            MPI.COMM_WORLD.Send(bufferStop, 0,1,MPI.INT,i,0);
        }

        int[] result = new int[2*aCoef.length-1];
        for(int i = 1 ;i<n;i++) {
            int[] currentResult = new int[2*aCoef.length-1];
            MPI.COMM_WORLD.Recv(currentResult, 0, 2*aCoef.length-1, MPI.INT, i, 0);

            for(int j = 0; j<2*aCoef.length-1;j++)
            {
                result[j] +=currentResult[j];
            }

        }

        var r = new Polynomial(result);
        System.out.println("Final result of rank " + MPI.COMM_WORLD.Rank() + " is: ");
        System.out.println(r.toString());

    }

    public static void partialO2(int polySize) {

        int[] first = new int[polySize];
        int[] second = new int[polySize];
        int[] start = new int[1];
        int[] stop = new int[1];
        int[] re = new int[2*polySize-1];

        MPI.COMM_WORLD.Recv(first, 0, polySize, MPI.INT, 0, 0);
        MPI.COMM_WORLD.Recv(second, 0, polySize, MPI.INT, 0, 0);
        MPI.COMM_WORLD.Recv(start, 0, 1, MPI.INT, 0, 0);
        MPI.COMM_WORLD.Recv(stop, 0, 1, MPI.INT, 0, 0);

        for (int i = start[0]; i < stop[0]; i++) {

            for (int j = 0; j < polySize; j++) {
                re[i + j] += first[i] * second[j];
            }
        }
        var a = new Polynomial(re);
        //System.out.println("Partial result of rank " + MPI.COMM_WORLD.Rank() + " is: ");
        System.out.println(a.toString());
        MPI.COMM_WORLD.Send(re, 0, re.length, MPI.INT, 0, 0);
    }

    public static void startKaratsuba(Polynomial a, Polynomial b){

        var result = new Polynomial(karatsubaRec(a.getCoefficients(),b.getCoefficients()));
        System.out.println(result);

    }

    public static int[] karatsubaRec(int[] a, int[] b)
    {
        //base case
        int[] product = new int[2*a.length];
        if(a.length==1){
            product[0] = a[0]*b[0];
            return product;
        }

        int halfSize = a.length/ 2;

        //half arrays
        var aLow = new int[halfSize];
        var aHigh = new int[halfSize];
        var bLow = new int[halfSize];
        var bHigh = new int[halfSize];
        var aLowHigh = new int[halfSize];
        var bLowHigh = new int[halfSize];
        //split data
        for (int i = 0; i < halfSize; i++) {
            aLow[i] = a[i];
            aHigh[i] = a[halfSize+i];
            aLowHigh[i] = aHigh[i] + aLow[i];

            bLow[i] = b[i];
            bHigh[i] = b[halfSize+i];
            bLowHigh[i] = bHigh[i] + bLow[i];
        }

        int[] resultLow, resultLowHigh;
        int[] resultHigh = new int[2*aHigh.length];

        if(MPI.COMM_WORLD.Size()>=3)
        {
            //make sure we have at least 3 processes to use
            //to split the poly in three parts
            int[] lengths = new int[4];
            lengths[0] = MPI.COMM_WORLD.Size()/3;
            lengths[1] = aLow.length;
            lengths[2] = bLow.length;
            lengths[3] = MPI.COMM_WORLD.Rank();
            MPI.COMM_WORLD.Send(lengths,0,lengths.length,MPI.INT,(MPI.COMM_WORLD.Rank()+lengths[0])%4,1);
            MPI.COMM_WORLD.Send(aLow,0,aLow.length,MPI.INT,(MPI.COMM_WORLD.Rank()+lengths[0])%4,2);
            MPI.COMM_WORLD.Send(bLow,0,bLow.length,MPI.INT,(MPI.COMM_WORLD.Rank()+lengths[0])%4,3);
            lengths[1] = aLowHigh.length;
            lengths[2] = bLowHigh.length;
            MPI.COMM_WORLD.Send(lengths,0,lengths.length,MPI.INT,(MPI.COMM_WORLD.Rank()+2*lengths[0])%4,1);
            MPI.COMM_WORLD.Send(aLowHigh,0,aLowHigh.length,MPI.INT,(MPI.COMM_WORLD.Rank()+2*lengths[0])%4,2);
            MPI.COMM_WORLD.Send(bLowHigh,0,bLowHigh.length,MPI.INT,(MPI.COMM_WORLD.Rank()+2*lengths[0])%4,3);

            resultHigh = karatsubaRec(aHigh,bHigh);

            //get the results
            int[] lowSize = new int[1];
            int[] lowHighSize = new int[1];
            MPI.COMM_WORLD.Recv(lowSize,0,1,MPI.INT,(MPI.COMM_WORLD.Rank()+lengths[0])%4,1);
            MPI.COMM_WORLD.Recv(lowHighSize,0,1,MPI.INT,(MPI.COMM_WORLD.Rank()+2*lengths[0])%4,1);
            resultLow = new int[lowSize[0]];
            resultLowHigh = new int[lowHighSize[0]];

            MPI.COMM_WORLD.Recv(resultLow,0,1,MPI.INT,(MPI.COMM_WORLD.Rank()+lengths[0])%4,2);
            MPI.COMM_WORLD.Recv(resultLowHigh,0,1,MPI.INT,(MPI.COMM_WORLD.Rank()+2*lengths[0])%4,2);

        }
        else
        {
            resultLow = karatsubaRec(aLow,bLow);
            resultHigh = karatsubaRec(aHigh,bHigh);
            resultLowHigh = karatsubaRec(aLowHigh,bLowHigh);
        }

        System.out.println(resultLow[0]);

        int[] resultMiddle = new int[a.length];


        for (int i = 0; i < a.length; ++i) {
            resultMiddle[i] = resultLowHigh[i] - resultLow[i] - resultHigh[i];
        }

        for (int i = 0; i < product.length; i++) {
            product[i] += resultLow[i];
            product[i + a.length] += resultHigh[i];
            product[i + halfSize] += resultMiddle[i];
        }

        return product;

    }

    static void partialKaratsuba(){
        int[] lenghts = new int[4];

        MPI.COMM_WORLD.Recv(lenghts,0,4,MPI.INT,MPI.ANY_SOURCE,1);
        int[] a = new int[lenghts[1]];
        int[] b = new int[lenghts[2]];

        int source = lenghts[3];
        MPI.COMM_WORLD.Recv(a,0,lenghts[1],MPI.INT,source,2);
        MPI.COMM_WORLD.Recv(b,0,lenghts[2],MPI.INT,source,3);


        int[] result = karatsubaRec(a,b);
        int[] resultSize = new int[1];
        resultSize[0] = result.length;

        MPI.COMM_WORLD.Send(resultSize,0,1,MPI.INT,source,1);
        MPI.COMM_WORLD.Send(result,0,result.length,MPI.INT,source,2);

    }



    public static void main(String[] args) {
        MPI.Init(args);
        int rank = MPI.COMM_WORLD.Rank();
        int polySize = 4;
        //main
        if(rank == 0)
        {
            //System.out.println(rank);
            var testPoli = new Polynomial(new int[]{1, 2, 1, 1});//,1,1,1,1,1,1,1,1,1,1,1,1)));
            var testPoli2 = new Polynomial(new int[]{1, 2, 1, 1});//,1,1,1,1,1,1,1,1,1,1,1,1)));

            //startO2(testPoli, testPoli2);
            startKaratsuba(testPoli, testPoli2);

        }
        else
        {
            //System.out.println(rank);
            //partialO2(polySize);
            partialKaratsuba();
        }

        //System.out.println("Hello world from <"+me+"> from <"+size);
        MPI.Finalize();
    }


}
