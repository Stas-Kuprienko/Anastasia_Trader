package com.anastasia.smart.algorithms.model;

public class SupportResistanceZone {
    private double minLevel;
    private double maxLevel;

    public SupportResistanceZone(double minLevel, double maxLevel) {
        this.minLevel = minLevel;
        this.maxLevel = maxLevel;
    }

    public double getMinLevel() {
        return minLevel;
    }

    public double getMaxLevel() {
        return maxLevel;
    }

    @Override
    public String toString() {
        return "SupportResistanceZone [minLevel=" + minLevel + ", maxLevel=" + maxLevel + "]";
    }
}
