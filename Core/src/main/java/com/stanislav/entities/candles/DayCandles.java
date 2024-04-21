package com.stanislav.entities.candles;

import java.util.Arrays;

public record DayCandles(DayCandle[] candles) implements Candles {

    @Override
    public String toString() {
        return "DayCandles{" +
                "candles=" + Arrays.toString(candles) +
                '}';
    }

    public record DayCandle (String date, Decimal open,
                          Decimal close, Decimal high,
                          Decimal low, int volume) implements Candles.Candle {

        @Override
        public String toString() {
            return "Candle{" +
                    "date='" + date + '\'' +
                    ", open=" + open +
                    ", close=" + close +
                    ", high=" + high +
                    ", low=" + low +
                    ", volume=" + volume +
                    '}';
        }
    }
}