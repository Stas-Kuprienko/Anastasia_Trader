package com.stanislav.entities.candles;

import java.time.LocalDateTime;
import java.util.Arrays;

public record IntraDayCandles(Candle[] candles) implements Candles {

    @Override
    public String toString() {
        return "IntraDayCandles{" +
                "candles=" + Arrays.toString(candles) +
                '}';
    }

    public record Candle (String timestamp, Decimal open,
                          Decimal close, Decimal high,
                          Decimal low, int volume) {

        @Override
        public String toString() {
            return "Candle{" +
                    "timestamp='" + timestamp + '\'' +
                    ", open=" + open +
                    ", close=" + close +
                    ", high=" + high +
                    ", low=" + low +
                    ", volume=" + volume +
                    '}';
        }
    }
}