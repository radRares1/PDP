package com.company;

import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        ArrayList<ArrayList<Integer>> graphOk = new ArrayList(List.of(
                new ArrayList(List.of(0, 1, 0, 1, 0)),
                new ArrayList(List.of(1, 0, 1, 1, 1)),
                new ArrayList(List.of(0, 1, 0, 0, 1)),
                new ArrayList(List.of(1, 1, 0, 0, 1)),
                new ArrayList(List.of(0, 1, 1, 1, 0))
        ));

        /*
           (0)--(1)--(2)
            |   / \   |
            |  /   \  |
            | /     \ |
           (3)-------(4)    */

        graphOk.forEach(System.out::println);

        ArrayList<ArrayList<Integer>> graphNotOk = new ArrayList(List.of(
                new ArrayList(List.of(0, 1, 0, 1, 0)),
                new ArrayList(List.of(1, 0, 1, 1, 1)),
                new ArrayList(List.of(0, 1, 0, 0, 1)),
                new ArrayList(List.of(1, 1, 0, 0, 0)),
                new ArrayList(List.of(0, 1, 1, 0, 0))

                /*
           (0)--(1)--(2)
            |   / \   |
            |  /   \  |
            | /     \ |
           (3)       (4)    */
        ));

        ArrayList<ArrayList<Integer>> bigGraph = new ArrayList(List.of(
                new ArrayList(List.of(0,1,0,1,1,0,0,0,1,0,1,1,0,0,0)),
                new ArrayList(List.of(1,0,1,0,0,1,0,0,0,1,0,0,1,0,0)),
                new ArrayList(List.of(0,1,0,1,0,0,1,0,1,0,0,0,1,1,0)),
                new ArrayList(List.of(1,0,1,0,0,0,0,1,0,0,0,0,1,0,1)),
                new ArrayList(List.of(1,0,0,0,0,1,0,1,0,1,1,1,0,0,0)),
                new ArrayList(List.of(0,1,0,0,1,0,1,0,0,0,0,0,0,0,1)),
                new ArrayList(List.of(0,0,1,0,0,1,0,1,1,0,0,1,0,0,1)),
                new ArrayList(List.of(0,0,0,1,1,0,1,0,0,0,1,0,1,0,1)),
                new ArrayList(List.of(1,0,1,0,0,0,1,0,0,0,0,1,0,0,0)),
                new ArrayList(List.of(0,1,0,0,1,0,0,0,0,0,0,0,0,0,0)),
                new ArrayList(List.of(1,0,0,0,1,0,0,1,0,0,0,0,0,0,0)),
                new ArrayList(List.of(1,0,0,0,1,0,1,0,1,0,0,0,1,0,1)),
                new ArrayList(List.of(0,1,1,1,0,0,0,1,0,0,0,1,0,1,0)),
                new ArrayList(List.of(0,0,1,0,0,0,0,0,0,0,0,0,1,0,0)),
                new ArrayList(List.of(0,0,0,1,0,1,1,1,0,0,0,1,0,0,0))));


        ArrayList<ArrayList<Integer>> bigGraphNoSol = new ArrayList(List.of(
                new ArrayList(List.of(0,0,0,0,1,0,0,0,1,0,1,1,0,0,0)),
                new ArrayList(List.of(0,0,0,0,0,1,0,0,0,1,0,0,1,0,0)),
                new ArrayList(List.of(0,0,0,0,0,0,1,0,1,0,0,0,1,1,0)),
                new ArrayList(List.of(0,0,0,0,0,0,0,1,0,0,0,0,1,0,1)),
                new ArrayList(List.of(1,0,0,0,0,1,0,1,0,1,1,1,0,0,0)),
                new ArrayList(List.of(0,1,0,0,1,0,1,0,0,0,0,0,0,0,1)),
                new ArrayList(List.of(0,0,1,0,0,1,0,1,1,0,0,1,0,0,1)),
                new ArrayList(List.of(0,0,0,1,1,0,1,0,0,0,1,0,1,0,1)),
                new ArrayList(List.of(1,0,1,0,0,0,1,0,0,0,0,1,0,0,0)),
                new ArrayList(List.of(0,1,0,0,1,0,0,0,0,0,0,0,0,0,0)),
                new ArrayList(List.of(1,0,0,0,1,0,0,1,0,0,0,0,0,0,0)),
                new ArrayList(List.of(1,0,0,0,1,0,1,0,1,0,0,0,1,0,0)),
                new ArrayList(List.of(0,1,1,1,0,0,0,1,0,0,0,1,0,0,0)),
                new ArrayList(List.of(0,0,1,0,0,0,0,0,0,0,0,0,0,0,0)),
                new ArrayList(List.of(0,0,0,1,0,1,1,1,0,0,0,0,0,0,0))));


        var startTime = System.currentTimeMillis()/1000.0;

        try {

            Utils u1 = new Utils(graphOk);
            u1.startSearch();

//            Utils u2 = new Utils(graphNotOk);
//            u2.startSearch();

//            Utils u3 = new Utils(bigGraph);
//            u3.startSearch();


        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }

        var endTime = System.currentTimeMillis()/1000.0;

        System.out.println(endTime - startTime);
    }
}
