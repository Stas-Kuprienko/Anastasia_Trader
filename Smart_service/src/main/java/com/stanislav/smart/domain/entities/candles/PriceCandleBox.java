package com.stanislav.smart.domain.entities.candles;

import java.util.List;

public interface PriceCandleBox {

    List<? extends PriceCandle> candles();
}
