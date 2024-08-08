package com.anastasia.smart.domain.market.finam.candles;

import com.anastasia.smart.entities.candles.DayCandle;
import com.anastasia.smart.entities.candles.DayCandleBox;
import java.util.List;

public record FinamDayCandleBoxProxy(List<? extends DayCandle> candles) implements DayCandleBox {}
