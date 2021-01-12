package com.company;

import java.util.Random;

public class Main {

    public static void main(String[] args) {
        int size = 5;
        Graph g = new Graph(size);

//        Random r = new Random();
//        int vertices = r.nextInt(6)+size;
//        for(int i=1;i<=vertices;i++)
//        {
//
//            int x = new Random().nextInt(size);
//            int y = new Random().nextInt(size);
//            while(y==x){
//                y = new Random().nextInt(size);
//            }
//            System.out.println(x+" "+y);
//            g.addEdge(x,y);
//
//        }


        g.addEdge(0, 1);
        g.addEdge(0, 2);
        g.addEdge(1, 2);
        g.addEdge(1, 3);
        g.addEdge(2, 3);
        g.addEdge(3, 4);

        /*
                _____
             /        \
           (0)--(1)--(2)
               /    /
              /   /
             /  /
           (3)-------(4)    */
//        var aux = g.graphAsAdjencyMatrix();
//        for(int i = 0; i<size;i++){
//            for(int j = 0; j<size;j++){
//                System.out.print(aux[i][j] + " ");
//            }
//            System.out.println("\n");
//        }


        long startTime = System.nanoTime();
        g.colorGraph();
        long endTime = System.nanoTime();

        long duration = (endTime - startTime);
        g.printColors();

        System.out.println("it took: " + (double)duration/1000000000 + " seconds.");
    }
}