package com.anastasia.smart.entities.candles;

import java.util.List;

public interface PriceCandleBox {

    List<? extends PriceCandle> candles();
}
