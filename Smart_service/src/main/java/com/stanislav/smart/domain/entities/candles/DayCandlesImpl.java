package com.stanislav.smart.domain.entities.candles;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.stanislav.smart.domain.entities.Decimal;

import java.util.Arrays;

public record DayCandlesImpl(DayCandle[] candles) implements DayCandles {

    @Override
    public String toString() {
        return "DayCandles{" +
                "candles=" + Arrays.toString(candles) +
                '}';
    }

    public record DayCandle (@JsonAlias("date") String dateTime,
                             Decimal open,
                             Decimal close,
                             Decimal high,
                             Decimal low,
                             long volume) implements Candles.Candle {

        @Override
        public String toString() {
            return "Candle{" +
                    "date='" + dateTime + '\'' +
                    ", open=" + open +
                    ", close=" + close +
                    ", high=" + high +
                    ", low=" + low +
                    ", volume=" + volume +
                    '}';
        }
    }
}