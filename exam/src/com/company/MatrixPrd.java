package com.company;

import mpi.MPI;


public class MatrixPrd {

    int processId, noProcesses;

    public int[] computeCol(int index,int[][] matrix){

        int[] res = new int[matrix.length];

        for(int i = 0; i<matrix.length;i++){
            res[i] = matrix[i][index];
        }

        return res;

    }

    public int[][] cmpute(int[][] a, int[][] b, int nrPrc){
        int[][] res = new int[a.length][a.length];//init as a


        for(int i = 0; i< a.length;i++){
            for(int j = 0; i< a[i].length;i++) {
                MPI.COMM_WORLD.Send(a.length,0,1,MPI.INT,(i+1)%nrPrc,0);
                MPI.COMM_WORLD.Send(a[i][j], 0, 1, MPI.INT, (i + 1) % nrPrc, 1);
                int[] bCol = computeCol(i, b);
                MPI.COMM_WORLD.Send(bCol, 0, b[i].length, MPI.INT, (i + 1) % nrPrc, 2);
                MPI.COMM_WORLD.Send(i,0,1,MPI.INT,(i+1)%nrPrc,3);
                MPI.COMM_WORLD.Send(j,0,1,MPI.INT,(i+1)%nrPrc,4);

            }
        }

        for(int k = 0; k< a.length;k++) {
            int temp = 0,i=0,j=0;
            MPI.COMM_WORLD.Recv(temp,0,1,MPI.INT,(k+1)%nrPrc,5);
            MPI.COMM_WORLD.Recv(i,0,1,MPI.INT,(k+1)%nrPrc,6);
            MPI.COMM_WORLD.Recv(j,0,1,MPI.INT,(k+1)%nrPrc,7);

            res[i][j] = temp;
        }

        return res;
    }

    private void worker(int processId, int noProcesses) {
        int len = 0,aEl=0,i=0,j=0;
        int[] bCol = new int[len];


        MPI.COMM_WORLD.Recv(len,0,1,MPI.INT,0,0);
        MPI.COMM_WORLD.Recv(aEl,0,len,MPI.INT,0,1);
        MPI.COMM_WORLD.Recv(bCol,0,len,MPI.INT,0,2);
        MPI.COMM_WORLD.Recv(i,0,1,MPI.INT,0,3);
        MPI.COMM_WORLD.Recv(j,0,1,MPI.INT,0,4);

        int res = 0;
        for(int element: bCol){
            res += element*aEl;
        }

        MPI.COMM_WORLD.Send(res,0,1,MPI.INT,0,5);
        MPI.COMM_WORLD.Send(i,0,1,MPI.INT,0,6);
        MPI.COMM_WORLD.Send(j,0,1,MPI.INT,0,7);

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
