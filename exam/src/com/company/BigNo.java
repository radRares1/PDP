package com.company;

import mpi.MPI;

public class BigNo {


    int[] mainProcess(int[] firstNo, int[] secondNo, int noProcesses) {
        int[] splitFirst, splitSecond;
        int[] result = new int[firstNo.length * 2];

        int sizePerProcess = firstNo.length / noProcesses;
        int reminder = firstNo.length % noProcesses;
        int start = 0, stop = 0;
        splitFirst = new int[firstNo.length];
        splitSecond = new int[sizePerProcess];
        int carryDigit = 0;

        for (int i = 1; i <= noProcesses; i++) {

            stop = start + sizePerProcess;
            if (reminder > 0) {
                stop++;
                reminder--;
            }

            //split
            for (int j = start; j < stop; j++) {
                splitSecond[j - start] = secondNo[j];
            }
            int sizeFirst = firstNo.length;
            int sizeSecond = stop - start;
            int resultSize = sizeFirst * 2;
            int[] partialRes = new int[resultSize];

            MPI.COMM_WORLD.Send(resultSize, 0, 1, MPI.INT, i, 0);
            MPI.COMM_WORLD.Send(sizeFirst, 0, 1, MPI.INT, i, 1);
            MPI.COMM_WORLD.Send(sizeSecond, 0, 1, MPI.INT, i, 2);
            MPI.COMM_WORLD.Send(start, 0, 1, MPI.INT, i, 3);

            MPI.COMM_WORLD.Send(splitFirst, 0, 1, MPI.INT, i, 4);
            MPI.COMM_WORLD.Send(splitSecond, 0, 1, MPI.INT, i, 5);

            MPI.COMM_WORLD.Recv(partialRes, 0, resultSize, MPI.INT, i, 6);

            carryDigit = 0;

            for (int j = 0; j < resultSize - 1; j++) {
                result[j] += partialRes[j] + carryDigit;
                carryDigit = result[j] / 10;
                result[j] %= 10;
            }

            start = stop;

        }

        if (carryDigit != 0) {
            result[result.length - 1] = carryDigit;
        }

        return result;

    }

    void worker() {

        int sizeFirst = 0, sizeSecond = 0, start = 0, resultSize = 0;
        int[] first,second,result;
        MPI.COMM_WORLD.Recv(resultSize, 0, 1, MPI.INT, 0, 0);
        MPI.COMM_WORLD.Recv(sizeFirst, 0, 1, MPI.INT, 0, 1);
        MPI.COMM_WORLD.Recv(sizeSecond, 0, 1, MPI.INT, 0, 2);
        MPI.COMM_WORLD.Recv(start, 0, 1, MPI.INT, 0, 3);
        first = new int[sizeFirst];
        second = new int[sizeSecond];
        result = new int[resultSize];
        MPI.COMM_WORLD.Recv(first, 0, sizeFirst, MPI.INT, 0, 4);
        MPI.COMM_WORLD.Recv(second, 0, sizeSecond, MPI.INT, 0, 5);

        for(int i = 0; i<sizeSecond; i++) {
            for (int j = 0; j < sizeFirst; j++) {

                int prod = first[j] * second[i];
                result[start+i+j] +=prod;

            }
        }

        MPI.COMM_WORLD.Send(result,0,resultSize,MPI.INT,0,6);

    }

}
