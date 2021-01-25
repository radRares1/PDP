package com.company;

import mpi.MPI;


public class MatrixElSum {

    int processId, noProcesses;

    public int cmpute(int[][] a, int nrPrc){
        int total = 0;


        for(int i = 0; i< a.length;i++){
            MPI.COMM_WORLD.Send(a.length,0,1,MPI.INT,(i+1)%nrPrc,0);
            MPI.COMM_WORLD.Send(a[i],0,a[i].length,MPI.INT,(i+1)%nrPrc,1);
        }

        for(int i = 0; i< a.length;i++) {
            int partialRes = 0;
            MPI.COMM_WORLD.Recv(partialRes,0,1,MPI.INT,(i+1)%nrPrc,2);
            total += partialRes;
        }

        return total;
    }

    private void worker() {
        int len = 0;
        int[] aRw = new int[len];

        MPI.COMM_WORLD.Recv(len,0,1,MPI.INT,0,0);
        MPI.COMM_WORLD.Recv(aRw,0,len,MPI.INT,0,1);

        int localRowResult = 0;
        for(int i = 0; i < len; i++){
            localRowResult += aRw[i];
        }

        MPI.COMM_WORLD.Send(localRowResult,0,1,MPI.INT,0,2);

    }


    public void main(String[] args) throws InterruptedException {
        MPI.Init(args);
        processId = MPI.COMM_WORLD.Rank();
        noProcesses = MPI.COMM_WORLD.Size();

        int[][] a = new int[5][5];

        if (processId == 0) {

            int res = cmpute(a,noProcesses);

        } else {
            worker();
        }

        MPI.Finalize();
    }


}
