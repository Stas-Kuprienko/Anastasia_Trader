package com.anastasia.smart.algorithms.model;

public class SupportZone {

    private double minLevel;
    private double maxLevel;
    private final double zoneWidth;

    public SupportZone(double initialLevel, double zoneWidth) {
        this.minLevel = initialLevel - zoneWidth;
        this.maxLevel = initialLevel + zoneWidth;
        this.zoneWidth = zoneWidth;
    }


    public void addLevel(double level) {
        minLevel = Math.min(minLevel, level - zoneWidth);
        maxLevel = Math.max(maxLevel, level + zoneWidth);
    }

    public double getCenter() {
        return (minLevel + maxLevel) / 2;
    }

    public double getMinLevel() {
        return minLevel;
    }

    public double getMaxLevel() {
        return maxLevel;
    }

    @Override
    public String toString() {
        return "SupportZone [minLevel=" + minLevel + ", maxLevel=" + maxLevel + "]";
    }
}