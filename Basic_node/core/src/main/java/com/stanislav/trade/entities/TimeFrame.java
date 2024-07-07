package com.stanislav.trade.entities;

import stanislav.anastasia.trade.Smart;

public final class TimeFrame {

    private TimeFrame() {}

    public enum IntraDay implements Scope {
        M1, M5, M15, H1
    }

    public enum Day implements Scope {
        D1, W1
    }

    public sealed interface Scope permits Day, IntraDay {}


    public static Scope parse(Smart.TimeFrame timeFrameProto) {
        return switch (timeFrameProto) {
            case D1 -> Day.D1;
            case W1 -> Day.W1;
            case H1 -> IntraDay.H1;
            case M15 -> IntraDay.M15;
            case M5 -> IntraDay.M5;
            case null, default -> throw new IllegalArgumentException("time_frame=" + timeFrameProto);
        };
    }

    public static Smart.TimeFrame parse(TimeFrame.Scope timeFrame) {
        return switch (timeFrame) {
            case Day.D1 -> Smart.TimeFrame.D1;
            case Day.W1 -> Smart.TimeFrame.W1;
            case IntraDay.H1 -> Smart.TimeFrame.H1;
            case IntraDay.M15 -> Smart.TimeFrame.M15;
            case IntraDay.M5 -> Smart.TimeFrame.M5;
            case null, default -> throw new IllegalArgumentException("time_frame=" + timeFrame);
        };
    }
}