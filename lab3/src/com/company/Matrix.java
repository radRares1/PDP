package com.company;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class Matrix {

    private int n;
    private List<ArrayList<Integer>> elements;

    public Matrix(int initN) {
        this.n = initN;

        elements = new ArrayList<ArrayList<Integer>>(n);
        for(int i=0;i<n;i++) {
            elements.add(new ArrayList<Integer>(n));
        }

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
