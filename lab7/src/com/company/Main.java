package com.company;

import mpi.MPI;

import java.util.ArrayList;
import java.util.List;

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

            startO2(testPoli, testPoli2);
            //startKaratsuba(Polynomial testPoli, Polynomial testPoli2);

        }
        else
        {
            //System.out.println(rank);
            partialO2(polySize);
        }

        //System.out.println("Hello world from <"+me+"> from <"+size);
        MPI.Finalize();
    }


}
