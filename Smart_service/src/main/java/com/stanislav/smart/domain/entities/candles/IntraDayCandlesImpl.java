package com.stanislav.smart.domain.entities.candles;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.stanislav.smart.domain.entities.Decimal;

import java.util.Arrays;

public record IntraDayCandlesImpl(Candle[] candles) implements IntraDayCandles {

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
                          long volume) implements Candles.Candle {

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