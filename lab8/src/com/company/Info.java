package com.company;

public class Info {
    int sourceId;
    int variable;
    int value;
    int compare;

    @Override
    public String toString() {
        return "Info{" +
                "sourceId=" + sourceId +
                ", variable=" + variable +
                ", value=" + value +
                ", compare=" + compare +
                '}';
    }

    public Info(int sourceId, int variable, int value, int compare) {
        this.sourceId = sourceId;
        this.variable = variable;
        this.value = value;
        this.compare = compare;
    }

    public Info(){

    }

    public int getSourceId() {
        return sourceId;
    }

    public void setSourceId(int sourceId) {
        this.sourceId = sourceId;
    }

    public int getVariable() {
        return variable;
    }

    public void setVariable(int variable) {
        this.variable = variable;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getCompare() {
        return compare;
    }

    public void setCompare(int compare) {
        this.compare = compare;
    }
}
