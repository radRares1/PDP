package com.company.entities;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Polynomial {

    ArrayList<Integer> coefficients ;

    public Polynomial(ArrayList<Integer> coeffs) {
        this.coefficients = coeffs;
    }

    public ArrayList<Integer> getCoefficients(){
        return this.coefficients;
    }

    public int getDegree() {
        return coefficients.size();
    }

    @Override
    public String toString() {

        StringBuilder result = new StringBuilder();
        for (int index = 0; index<coefficients.size();index++) {
            if(coefficients.get(index) !=0) {
                String sign = (coefficients.get(index) > 0) ? " + " : " - ";
                result.append(sign + coefficients.get(index) + "*x^" + (coefficients.size() - index - 1) + " ");
            }
        }
        return result.toString();
    }


}
