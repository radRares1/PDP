package com.company;

import mpi.MPI;


public class ScalarPrd {

    int processId, noProcesses;

    public int cmpute(int[] a,int[] b, int nrPrc){
        noProcesses--;
        int sizePerProcess = a.length/noProcesses;
        int reminder = a.length%noProcesses;
        int start=0,stop=0;
        int[] splitA = new int[sizePerProcess+1];
        int[] splitB = new int[sizePerProcess+1];
        int total = 0;
        int partialSum = 0;

        for(int i = 0; i< noProcesses;i++){

            stop = start + sizePerProcess;
            if(reminder > 0){
                stop ++;
                reminder--;
            }

            for(int j = start;j<stop; j++){
                splitA[j-start] = a[j];
                splitB[j-start] = b[j];
            }

            int sizeOfCoefPerProcess = stop-start;


            MPI.COMM_WORLD.Send(sizeOfCoefPerProcess,0,1,MPI.INT,i,0);
            MPI.COMM_WORLD.Send(splitA,0,sizeOfCoefPerProcess,MPI.INT,i,1);
            MPI.COMM_WORLD.Send(splitB,0,sizeOfCoefPerProcess,MPI.INT,i,2);
            start = stop;

            MPI.COMM_WORLD.Recv(partialSum,0,1,MPI.INT,i,4);
            total += partialSum;
        }

        return total;
    }

    private void worker(int processId, int noProcesses) {
        int sizeOfSplit = 0;
        MPI.COMM_WORLD.Recv(sizeOfSplit,0,1,MPI.INT,0,0);


        int[] aElems = new int[sizeOfSplit];
        int[] bElems = new int[sizeOfSplit];

        MPI.COMM_WORLD.Recv(aElems,0,sizeOfSplit,MPI.INT,0,1);
        MPI.COMM_WORLD.Recv(bElems,0,sizeOfSplit,MPI.INT,0,2);

        int localResult = 0;
        for (int i = 0; i < sizeOfSplit; i++) {
            localResult += aElems[i]*bElems[i];
        }

        MPI.COMM_WORLD.Send(localResult,0,1,MPI.INT,0,3);

    }


    public void main(String[] args) throws InterruptedException {
        MPI.Init(args);
        processId = MPI.COMM_WORLD.Rank();
        noProcesses = MPI.COMM_WORLD.Size();

        int[] a = new int[5];
        int[] b = new int[5];

        if (processId == 0) {
            int res = cmpute(a,b,noProcesses);

        } else {
            worker(processId,noProcesses);
        }

        MPI.Finalize();
    }


}
