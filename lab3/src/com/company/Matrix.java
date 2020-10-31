package com.company;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

public class Matrix {

    private int n,currentElementi,currentElementj;
    private List<ArrayList<Integer>> elements;

    public int getCurrentElementi() {
        return currentElementi;
    }

    public void setCurrentElementi(int currentElementi) {
        this.currentElementi = (currentElementi%n);
    }

    public int getCurrentElementj() {
        return currentElementj;
    }

    public void setCurrentElementj(int currentElementj) {
        this.currentElementj = (currentElementj%n);
    }

    public Matrix(int initN) {
        this.n = initN;

        elements = new ArrayList<ArrayList<Integer>>(n);
        for(int i=0;i<n;i++) {
            elements.add(new ArrayList<Integer>(Collections.nCopies(n, 0)));
        }

        currentElementi=0;
        currentElementj=0;

    }

    public int getSize() { return n;}

    public List<ArrayList<Integer>> getElements() {
        return this.elements;
    }

    public int getElement(int i, int j){
        return elements.get(i).get(j);
    }

    public void setElements(ArrayList<ArrayList<Integer>> newElements) {elements = newElements;}

    public void setElement(int i, int j, int value){
        //System.out.println(elements.get(i).isEmpty());
        //System.out.println(Arrays.toString(elements.get(i).toArray()));

        //elements.get(i).add(j,value);
        elements.get(i).set(j,value);
    }

    public ArrayList<Integer> getRow(int index) {
        return elements.get(index);
    }

    public ArrayList<Integer> getCol(int index) {
        var collector = new ArrayList<Integer>(n);
        IntStream.range(0,n).forEach(e ->collector.add(elements.get(e).get(index)));
        return collector;

    }

}
