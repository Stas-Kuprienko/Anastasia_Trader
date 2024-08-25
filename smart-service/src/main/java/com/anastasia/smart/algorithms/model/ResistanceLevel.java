package com.anastasia.smart.algorithms.model;

public class ResistanceLevel extends PriceLevel {

    public ResistanceLevel(double level) {
        super(level);
    }

    @Override
    public String toString() {
        return "ResistanceLevel{" +
                "level=" + level +
                ", touches=" + touches.size() +
                '}';
    }
}