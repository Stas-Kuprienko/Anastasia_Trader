package com.stanislav.smart.domain.entities.candles;

import com.stanislav.smart.domain.entities.Decimal;

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
