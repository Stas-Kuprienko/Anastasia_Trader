package com.stanislav.entities.candles;

import java.util.Arrays;

public record IntraDayCandles(IntraDayCandleDTO[] candles) implements Candles {

    @Override
    public String toString() {
        return "IntraDayCandles{" +
                "candles=" + Arrays.toString(candles) +
                '}';
    }

    public record IntraDayCandleDTO(String timestamp, Decimal open,
                                    Decimal close, Decimal high,
                                    Decimal low, int volume) {}
}