package com.stanislav.smart.domain.market.finam.candles;

import com.stanislav.smart.domain.entities.candles.DayCandle;
import com.stanislav.smart.domain.entities.candles.DayCandleBox;
import java.util.List;

public record FinamDayCandleBoxProxy(List<? extends DayCandle> candles) implements DayCandleBox {}