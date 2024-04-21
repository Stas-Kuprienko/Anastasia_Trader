package com.stanislav.entities.candles;

public interface Candles {

    Candle[] candles();

    interface Candle {
        Decimal open();
        Decimal close();
        Decimal high();
        Decimal low();
        int volume();
    }
}
