package com.stanislav.smart_analytics.analysis.candles.candles;

public interface Candles {

    Candle[] candles();

    interface Candle {
        String dateTime();
        Decimal open();
        Decimal close();
        Decimal high();
        Decimal low();
        int volume();
    }
}
