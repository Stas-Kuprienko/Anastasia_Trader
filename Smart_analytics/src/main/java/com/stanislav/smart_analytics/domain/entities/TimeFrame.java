package com.stanislav.smart_analytics.domain.entities;

import proto.tradeapi.v1.Candles;

public final class TimeFrame {

    private TimeFrame() {}

    public enum IntraDay {
        M1, M5, M15, H1
    }

    public enum Day {
        D1, W1
    }

    public static TimeFrame.IntraDay fromProto(Candles.IntradayCandleTimeFrame proto) {
        IntraDay timeFrame;
        switch (proto) {
            case INTRADAYCANDLE_TIMEFRAME_M1 -> timeFrame = IntraDay.M1;
            case INTRADAYCANDLE_TIMEFRAME_M5 -> timeFrame = IntraDay.M5;
            case INTRADAYCANDLE_TIMEFRAME_M15 -> timeFrame = IntraDay.M15;
            case INTRADAYCANDLE_TIMEFRAME_H1 -> timeFrame = IntraDay.H1;
            default -> throw new EnumConstantNotPresentException(TimeFrame.IntraDay.class, proto.name());
        }
        return timeFrame;
    }

    public static TimeFrame.Day fromProto(Candles.DayCandleTimeFrame proto) {
        Day timeFrame;
        switch (proto) {
            case DAYCANDLE_TIMEFRAME_D1 -> timeFrame = Day.D1;
            case DAYCANDLE_TIMEFRAME_W1 -> timeFrame = Day.W1;
            default -> throw new EnumConstantNotPresentException(TimeFrame.Day.class, proto.name());
        }
        return timeFrame;
    }

    public static Candles.IntradayCandleTimeFrame toProtoValue(TimeFrame.IntraDay timeFrame) {
        Candles.IntradayCandleTimeFrame proto;
        switch (timeFrame) {
            case M1 -> proto = Candles.IntradayCandleTimeFrame.INTRADAYCANDLE_TIMEFRAME_M1;
            case M5 -> proto = Candles.IntradayCandleTimeFrame.INTRADAYCANDLE_TIMEFRAME_M5;
            case M15 -> proto = Candles.IntradayCandleTimeFrame.INTRADAYCANDLE_TIMEFRAME_M15;
            case H1 -> proto = Candles.IntradayCandleTimeFrame.INTRADAYCANDLE_TIMEFRAME_H1;
            default -> throw new EnumConstantNotPresentException(Candles.IntradayCandleTimeFrame.class, timeFrame.name());
        }
        return proto;
    }

    public static Candles.DayCandleTimeFrame toProtoValue(TimeFrame.Day timeFrame) {
        Candles.DayCandleTimeFrame proto;
        switch (timeFrame) {
            case D1 -> proto = Candles.DayCandleTimeFrame.DAYCANDLE_TIMEFRAME_D1;
            case W1 -> proto = Candles.DayCandleTimeFrame.DAYCANDLE_TIMEFRAME_W1;
            default -> throw new EnumConstantNotPresentException(Candles.DayCandleTimeFrame.class, timeFrame.name());
        }
        return proto;
    }
}
