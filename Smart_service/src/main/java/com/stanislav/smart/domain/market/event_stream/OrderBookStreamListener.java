package com.stanislav.smart.domain.market.event_stream;

import com.stanislav.smart.entities.Stoppable;

public interface OrderBookStreamListener extends Stoppable {

    OrderBookCollector orderBookCollector();

    interface OrderBookCollector {

        OrderBookRow currentAsk();

        OrderBookRow currentBid();

        double lastAskPrice();

        double lastBidPrice();
    }
}
