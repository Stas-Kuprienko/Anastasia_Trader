package com.anastasia.smart.algorithms.model;

import java.util.Objects;

public abstract class PriceZone {

    protected final double zoneWidth;
    protected int touches;
    protected double minLevel;
    protected double maxLevel;
    protected long totalVolume;
    protected int weight;

    public PriceZone(double initialLevel, double zoneWidth, int touches, long totalVolume) {
        this.minLevel = initialLevel;
        this.maxLevel = initialLevel;
        this.zoneWidth = zoneWidth;
        this.touches = touches;
        this.totalVolume = totalVolume;
    }

    public void addLevel(double level, int touches, int volume) {
        minLevel = Math.min(minLevel, level - zoneWidth);
        maxLevel = Math.max(maxLevel, level + zoneWidth);
        this.touches += touches;
        totalVolume += volume;
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

    public int getTouches() {
        return touches;
    }

    public long getTotalVolume() {
        return totalVolume;
    }

    public long getAverageVolume() {
        return totalVolume / touches;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PriceZone priceZone)) return false;
        return Double.compare(zoneWidth, priceZone.zoneWidth) == 0 &&
                touches == priceZone.touches &&
                Double.compare(minLevel, priceZone.minLevel) == 0 &&
                Double.compare(maxLevel, priceZone.maxLevel) == 0 &&
                totalVolume == priceZone.totalVolume &&
                weight == priceZone.weight;
    }

    @Override
    public int hashCode() {
        return Objects.hash(zoneWidth, touches, minLevel, maxLevel, totalVolume, weight);
    }
}
