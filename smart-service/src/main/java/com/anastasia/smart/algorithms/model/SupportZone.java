package com.anastasia.smart.algorithms.model;

public class SupportZone extends PriceZone {

    public SupportZone(double initialLevel, double zoneWidth, int touches, int volume) {
        super(initialLevel, zoneWidth, touches, volume);
    }

    @Override
    public String toString() {
        return "SupportZone{" +
                "zoneWidth=" + zoneWidth +
                ", minLevel=" + minLevel +
                ", maxLevel=" + maxLevel +
                '}';
    }
}