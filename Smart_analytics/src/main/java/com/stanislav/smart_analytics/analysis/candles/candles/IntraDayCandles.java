package com.stanislav.smart_analytics.analysis.candles.candles;

import com.fasterxml.jackson.annotation.JsonAlias;

import java.util.Arrays;

public record IntraDayCandles(Candle[] candles) implements Candles {

    @Override
    public String toString() {
        return "IntraDayCandles{" +
                "candles=" + Arrays.toString(candles) +
                '}';
    }

    public record Candle (@JsonAlias("timestamp") String dateTime,
                          Decimal open,
                          Decimal close,
                          Decimal high,
                          Decimal low,
                          int volume) implements Candles.Candle {

        @Override
        public String toString() {
            return "Candle{" +
                    "timestamp='" + dateTime + '\'' +
                    ", open=" + open +
                    ", close=" + close +
                    ", high=" + high +
                    ", low=" + low +
                    ", volume=" + volume +
                    '}';
        }
    }
}