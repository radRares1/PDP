package com.company;

import mpi.MPI;

import java.util.ArrayList;
import java.util.stream.IntStream;

public class Perm {

    private void swap(int[] input, int a, int b) {
        int tmp = input[a];
        input[a] = input[b];
        input[b] = tmp;
    }

    public int[][] generateAll(int n, int[] elements) {
        int[] indexes = new int[n];
        int nFactorial = IntStream.rangeClosed(1, n)
                .reduce(1, (int x, int y) -> x * y);
        int[][] result = new int[elements.length][nFactorial];
        ArrayList<int[]> resultList = new ArrayList();
        for (int i = 0; i < n; i++) {
            indexes[i] = 0;
        }

        int i = 0;
        while (i < n) {
            if (indexes[i] < i) {
                swap(elements, i % 2 == 0 ?  0: indexes[i], i);
                resultList.add(elements);
                indexes[i]++;
                i = 0;
            }
            else {
                indexes[i] = 0;
                i++;
            }
        }

        for(int j = 0; j<resultList.size();j++){
            result[i] = resultList.get(i);
        }
    return result;
    }

    void mainProcess(int[] elements, int noProcesses){

        int total = 0;

        int[][] allPerms = generateAll(elements.length,elements);

        for(int i = 0; i<allPerms.length;i++){
            MPI.COMM_WORLD.Send(allPerms[i].length,0,1,MPI.INT,(i+1)%noProcesses,0);
            MPI.COMM_WORLD.Send(allPerms[i],0,allPerms[i].length,MPI.INT,(i+1)%noProcesses,1);
        }

        for(int i = 0; i< allPerms.length;i++) {
            int partialResult = 0;
            MPI.COMM_WORLD.Recv(partialResult,0,1,MPI.INT,(i+1)%noProcesses,2);
            total +=  partialResult;
        }

        System.out.println("There are "+ total + " permutations that satisfy the condition");

    }

    boolean satisfiesCondition(int[] perm)
    {
        return true;
    }

    void worker(){

        int len = 0;
        int[] perm = new int[len];

        MPI.COMM_WORLD.Recv(len,0,1,MPI.INT,0,0);
        MPI.COMM_WORLD.Recv(perm,0,len,MPI.INT,0,1);

        int localResult = 0;
        if(satisfiesCondition(perm)) {
            localResult = 1;
        }

        MPI.COMM_WORLD.Send(localResult,0,1,MPI.INT,0,2);

    }



}
