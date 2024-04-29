package com.stanislav.smart_analytics.domain.analysis.candles.candles;

import com.fasterxml.jackson.annotation.JsonAlias;

import java.util.Arrays;

public record DayCandles(DayCandle[] candles) implements Candles {

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
                             int volume) implements Candles.Candle {

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