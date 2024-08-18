package com.anastasia.smart.algorithms.model;

import java.util.ArrayList;
import java.util.List;

public class ResistanceLevel {

    private final double level;
    private final List<PricePoint> touches;

    public ResistanceLevel(double level) {
        this.level = level;
        this.touches = new ArrayList<>();
    }

    public double getLevel() {
        return level;
    }

    public int getTouchCount() {
        return touches.size();
    }

    public void addTouch(PricePoint touch) {
        touches.add(touch);
    }
}