package com.anastasia.smart.algorithms.model;

public class SupportLevel extends PriceLevel {

    public SupportLevel(double level) {
        super(level);
    }

    @Override
    public String toString() {
        return "SupportLevel{" +
                "level=" + level +
                ", touches=" + touches.size() +
                '}';
    }
}