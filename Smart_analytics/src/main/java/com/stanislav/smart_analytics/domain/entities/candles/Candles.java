package com.stanislav.smart_analytics.domain.entities.candles;

public interface Candles {

    Candle[] candles();

    interface Candle {
        String dateTime();
        Decimal open();
        Decimal close();
        Decimal high();
        Decimal low();
        long volume();
    }
}
