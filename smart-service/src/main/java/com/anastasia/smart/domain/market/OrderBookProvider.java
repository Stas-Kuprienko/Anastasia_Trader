package com.anastasia.smart.domain.market;

import com.anastasia.smart.domain.automation.TradeStrategy;
import com.anastasia.smart.domain.market.order_book.OrderBookStream;
import com.anastasia.trade.Smart;

public interface OrderBookProvider <ORDER_BOOK_ROW> {

    OrderBookStream<ORDER_BOOK_ROW> subscribe(Smart.Security security, TradeStrategy strategy);

    void unsubscribe(Smart.Security security);
}
