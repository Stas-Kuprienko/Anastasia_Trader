package com.anastasia.smart.algorithms.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class PriceLevel {

    protected final double level;
    protected final List<PricePoint> touches;

    public PriceLevel(double level) {
        this.level = level;
        this.touches = new ArrayList<>();
    }

    public double getLevel() {
        return level;
    }

    public List<PricePoint> getTouches() {
        return touches;
    }

    public int getTouchCount() {
        return touches.size();
    }

    public void addTouch(PricePoint touch) {
        touches.add(touch);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PriceLevel that)) return false;
        return Double.compare(level, that.level) == 0 && Objects.equals(touches, that.touches);
    }

    @Override
    public int hashCode() {
        return Objects.hash(level, touches);
    }
}
