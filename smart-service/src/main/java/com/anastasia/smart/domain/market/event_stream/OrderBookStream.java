package com.anastasia.smart.domain.market.event_stream;

import com.anastasia.trade.Smart;

public interface OrderBookStream {

    OrderBookStreamListener subscribe(Smart.Security security);

    void unsubscribe(OrderBookStreamListener listener);

    void unsubscribe(OrderBookStreamListener listener, boolean isForced);

    OrderBookStreamListener getListener(Smart.Security security);
}
