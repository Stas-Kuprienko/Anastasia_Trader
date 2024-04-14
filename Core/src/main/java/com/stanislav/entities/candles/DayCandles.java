package com.stanislav.entities.candles;

import java.time.LocalDate;
import java.util.Arrays;

public record DayCandles(Candle[] candles) implements Candles {

    @Override
    public String toString() {
        return "DayCandles{" +
                "candles=" + Arrays.toString(candles) +
                '}';
    }

    public record Candle (String date, Decimal open,
                          Decimal close, Decimal high,
                          Decimal low, int volume) {

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