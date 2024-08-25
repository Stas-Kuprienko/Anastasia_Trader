package com.anastasia.smart.algorithms.model;

public class ResistanceZone extends PriceZone {

    public ResistanceZone(double initialLevel, double zoneWidth, int touches, int volume) {
        super(initialLevel, zoneWidth, touches, volume);
    }

    @Override
    public String toString() {
        return "ResistanceZone{" +
                "zoneWidth=" + zoneWidth +
                ", minLevel=" + minLevel +
                ", maxLevel=" + maxLevel +
                '}';
    }
}
