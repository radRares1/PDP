package com.company;

import mpi.MPI;

import java.util.ArrayList;

public class Primes {

    int[] primes(int n, int nrPrc) {

        return new int[0];
    }

    void wrker() {
        int firstSize=0,n=0,stA=0,stB=0;

        MPI.COMM_WORLD.Recv(firstSize, 0, 1, MPI.INT, 0, 0);
        int[] firstPrimes = new int[firstSize];
        MPI.COMM_WORLD.Recv(firstPrimes, 0, firstSize, MPI.INT, 0, 1);
        MPI.COMM_WORLD.Recv(n, 0, 1, MPI.INT, 0, 2);

        boolean[] arePrimes = new boolean[n];

        MPI.COMM_WORLD.Recv(stA, 0, 1, MPI.INT, 0, 3);
        MPI.COMM_WORLD.Recv(stB, 0, 1, MPI.INT, 0, 4);

        for(int i = stA; i < stB;i++){
            boolean isPrime = true;
            for(int j = 0; j< firstSize;j++){
                if(i % firstPrimes[j] == 0){
                    isPrime = false;
                    break;
                }
            }
            if(isPrime){
                arePrimes[i] = true;
            }
        }

        MPI.COMM_WORLD.Send(arePrimes,0,arePrimes.length,MPI.BOOLEAN,0,5);

    }


    void main(String[] args) {

        int n = 100, rank, nrPrc;

        MPI.Init(args);
        rank = MPI.COMM_WORLD.Rank();
        nrPrc = MPI.COMM_WORLD.Size();

        if (rank == 0) {
            nrPrc--;

            int sqN = (int) Math.sqrt(n);
            boolean[] primes = new boolean[n];


            for (int i = 0; i < n; i++) {
                primes[i] = false;
            }
            ArrayList<Integer> firstSqrtNPrimes = new ArrayList<>();

            for (int i = 2; i <= sqN; i++) {
                boolean isPrime = true;
                for (int j = 2; j <= i / 2; j++) {
                    if (i % j == 0) {
                        isPrime = false;
                        break;
                    }
                }
                if (isPrime) {
                    firstSqrtNPrimes.add(i);
                }
            }

            int[] primesArray = new int[firstSqrtNPrimes.size()];

            for (int i = 0; i < firstSqrtNPrimes.size(); i++) {

                primes[firstSqrtNPrimes.get(i)] = true;
                primesArray[i] = firstSqrtNPrimes.get(i);
            }

            int sizeWithoutSqrt = n - sqN;
            boolean[] res = new boolean[n];
            int sizePerPrc = sizeWithoutSqrt / nrPrc;
            int reminder = sizeWithoutSqrt % nrPrc;
            int start = 0, stop = 0;

            for (int i = 1; i <= nrPrc; i++) {

                stop = start + sizePerPrc + (reminder>0 ? 1 : 0);
                if(reminder==1){
                    reminder--;
                }

                int firstSize = firstSqrtNPrimes.size();
                int stA = start + sqN;
                int stB = stop + sqN;

                MPI.COMM_WORLD.Send(firstSize,0,1,MPI.INT,i,0);
                MPI.COMM_WORLD.Send(primesArray,0,firstSize,MPI.INT,i,1);
                MPI.COMM_WORLD.Send(n,0,1,MPI.INT,i,2);
                MPI.COMM_WORLD.Send(stA,0,1,MPI.INT,i,3);
                MPI.COMM_WORLD.Send(stB,0,1,MPI.INT,i,4);

                MPI.COMM_WORLD.Recv(res, 0, n, MPI.BOOLEAN, i, 5);

                for(int j = 0; j<n;j++){
                    if(res[j]){
                        primes[j] = true;
                    }
                }
                start = stop;

                for(int k = 0; k<n;k++){
                    if(primes[k])
                        System.out.println(k);
                }

            }


        }
        else {
            wrker();
        }

    }


}
