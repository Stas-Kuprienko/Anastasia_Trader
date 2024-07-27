package com.stanislav.ui.model;

public final class TimeFrame {

    private TimeFrame() {}

    public enum IntraDay implements Scope {
        M1, M5, M15, H1
    }

    public enum Day implements Scope {
        D1, W1
    }

    public sealed interface Scope permits Day, IntraDay {}


    public static Scope valueOf(String value) {
        return switch (value) {
            case "M1" -> IntraDay.M1;
            case "M5" -> IntraDay.M5;
            case "M15" -> IntraDay.M15;
            case "H1" -> IntraDay.H1;
            case "D1" -> Day.D1;
            case "W1" -> Day.W1;
            case null, default -> throw new IllegalArgumentException("value=" + value);
        };
    }
}