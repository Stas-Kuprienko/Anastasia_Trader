package com.anastasia.smart.algorithms.model;

public class ResistanceZone {
    private double minLevel;
    private double maxLevel;
    private double zoneWidth;

    public ResistanceZone(double initialLevel, double zoneWidth) {
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
        return "ResistanceZone [minLevel=" + minLevel + ", maxLevel=" + maxLevel + "]";
    }
}
