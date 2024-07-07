package com.stanislav.smart.domain.market.finam.candles;

import com.stanislav.smart.domain.entities.candles.IntraDayCandle;
import com.stanislav.smart.domain.entities.candles.IntraDayCandleBox;
import java.util.List;

public record FinamIntraDayCandleBoxProxy(List<? extends IntraDayCandle> candles) implements IntraDayCandleBox {}
