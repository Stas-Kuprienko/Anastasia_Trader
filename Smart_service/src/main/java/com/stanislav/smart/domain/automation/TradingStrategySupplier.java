package com.stanislav.smart.domain.automation;

import com.stanislav.smart.domain.entities.TimeFrame;
import stanislav.anastasia.trade.Smart;

public interface TradingStrategySupplier<TS extends TradingStrategy> {

    TS supply(Smart.Security security, TimeFrame.Scope timeFrame);

    String getName();
}
