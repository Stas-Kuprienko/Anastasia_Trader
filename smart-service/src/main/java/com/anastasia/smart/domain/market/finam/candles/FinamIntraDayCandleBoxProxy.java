package com.anastasia.smart.domain.market.finam.candles;

import com.anastasia.smart.entities.candles.IntraDayCandle;
import com.anastasia.smart.entities.candles.IntraDayCandleBox;
import java.util.List;

public record FinamIntraDayCandleBoxProxy(List<? extends IntraDayCandle> candles) implements IntraDayCandleBox {}
