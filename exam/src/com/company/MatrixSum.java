package com.company;

import mpi.MPI;

import java.util.ArrayList;

public class MatrixSum {

    int processId, noProcesses;
    ArrayList<ArrayList<Integer>> memory = new ArrayList<>();

    public int[][] cmpute(int[][] a, int[][] b, int nrPrc){
        int[][] res = new int[a.length][a.length];//init as a


        for(int i = 0; i<a.length;i++){
            MPI.COMM_WORLD.Send(a.length,0,1,MPI.INT,(i+1)%nrPrc,0);
            MPI.COMM_WORLD.Send(a[i],0,a[i].length,MPI.INT,(i+1)%nrPrc,1);
            MPI.COMM_WORLD.Send(b[i],0,b[i].length,MPI.INT,(i+1)%nrPrc,2);
        }

        for(int i = 0; i< a.length;i++) {
            int[] temp = new int[a[0].length];
            MPI.COMM_WORLD.Recv(temp,0,a[i].length,MPI.INT,(i+1)%nrPrc,3);
            res[i] = temp;
        }

        return res;
    }

    private void worker(int processId, int noProcesses) {
        int len = 0;
        int[] aRw = new int[len];//init as a
        int[] bRw = new int[len];//init as a


        MPI.COMM_WORLD.Recv(len,0,1,MPI.INT,0,0);
        MPI.COMM_WORLD.Recv(aRw,0,len,MPI.INT,0,1);
        MPI.COMM_WORLD.Recv(bRw,0,len,MPI.INT,0,2);

        int[] resRw = new int[len];
        for(int i = 0; i < len; i++){
            resRw[i] = aRw[i] + bRw[i];
        }

        MPI.COMM_WORLD.Send(resRw,0,resRw.length,MPI.INT,0,3);

    }


    public void main(String[] args) throws InterruptedException {
        MPI.Init(args);
        processId = MPI.COMM_WORLD.Rank();
        noProcesses = MPI.COMM_WORLD.Size();

        int[][] a = new int[5][5];
        int[][] b = new int[5][5];

        if (processId == 0) {

            int[][] res = cmpute(a,b,noProcesses);

        } else {
            worker(processId,noProcesses);
        }

        MPI.Finalize();
    }


}
