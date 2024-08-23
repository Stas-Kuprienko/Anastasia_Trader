package com.anastasia.smart.domain.market;

import com.anastasia.smart.domain.automation.TradeStrategy;
import com.anastasia.smart.domain.market.order_book.OrderBookStreamWrapper;
import com.anastasia.trade.Smart;

public interface OrderBookProvider {

    OrderBookStreamWrapper subscribe(Smart.Security security, TradeStrategy strategy);

    void unsubscribe(Smart.Security security);
}
