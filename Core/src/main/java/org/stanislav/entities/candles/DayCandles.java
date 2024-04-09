package org.stanislav.entities.candles;

import java.util.Arrays;

public record DayCandles(DayCandleDTO[] candles) implements Candles {

    @Override
    public String toString() {
        return "DayCandles{" +
                "candles=" + Arrays.toString(candles) +
                '}';
    }

    public record DayCandleDTO(String date, Decimal open,
                               Decimal close, Decimal high,
                               Decimal low, int volume) {}
}