package com.stanislav.smart.domain.automation;

import com.stanislav.smart.entities.TimeFrame;
import stanislav.anastasia.trade.Smart;

public interface TradeStrategySupplier<TS extends TradeStrategy> {

    TS supply(Smart.Security security, TimeFrame.Scope timeFrame);

    String getName();
}
