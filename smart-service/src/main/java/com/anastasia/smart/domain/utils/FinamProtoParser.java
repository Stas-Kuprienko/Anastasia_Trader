package com.anastasia.smart.domain.utils;

import com.anastasia.smart.entities.TimeFrame;
import proto.tradeapi.v1.Candles;

public final class FinamProtoParser {

    private FinamProtoParser(){}


    public static TimeFrame.IntraDay fromProto(Candles.IntradayCandleTimeFrame proto) {
        TimeFrame.IntraDay timeFrame;
        switch (proto) {
            case INTRADAYCANDLE_TIMEFRAME_M1 -> timeFrame = TimeFrame.IntraDay.M1;
            case INTRADAYCANDLE_TIMEFRAME_M5 -> timeFrame = TimeFrame.IntraDay.M5;
            case INTRADAYCANDLE_TIMEFRAME_M15 -> timeFrame = TimeFrame.IntraDay.M15;
            case INTRADAYCANDLE_TIMEFRAME_H1 -> timeFrame = TimeFrame.IntraDay.H1;
            default -> throw new EnumConstantNotPresentException(TimeFrame.IntraDay.class, proto.name());
        }
        return timeFrame;
    }

    public static TimeFrame.Day fromProto(Candles.DayCandleTimeFrame proto) {
        TimeFrame.Day timeFrame;
        switch (proto) {
            case DAYCANDLE_TIMEFRAME_D1 -> timeFrame = TimeFrame.Day.D1;
            case DAYCANDLE_TIMEFRAME_W1 -> timeFrame = TimeFrame.Day.W1;
            default -> throw new EnumConstantNotPresentException(TimeFrame.Day.class, proto.name());
        }
        return timeFrame;
    }

    public static Candles.IntradayCandleTimeFrame toProto(TimeFrame.IntraDay timeFrame) {
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

    public static Candles.DayCandleTimeFrame toProto(TimeFrame.Day timeFrame) {
        Candles.DayCandleTimeFrame proto;
        switch (timeFrame) {
            case D1 -> proto = Candles.DayCandleTimeFrame.DAYCANDLE_TIMEFRAME_D1;
            case W1 -> proto = Candles.DayCandleTimeFrame.DAYCANDLE_TIMEFRAME_W1;
            default -> throw new EnumConstantNotPresentException(Candles.DayCandleTimeFrame.class, timeFrame.name());
        }
        return proto;
    }
}
