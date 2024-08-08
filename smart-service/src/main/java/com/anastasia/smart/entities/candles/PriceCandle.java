package com.anastasia.smart.entities.candles;

import java.math.BigDecimal;

public interface PriceCandle {

    String dateTime();

    BigDecimal open();

    BigDecimal close();

    BigDecimal high();

    BigDecimal low();

    long volume();
}
