/*
 * Stanislav Kuprienko *** Omsk, Russia
 */

/*
 * Stanislav Kuprienko *** Omsk, Russia
 */

package com.stanislav.smart.domain.automation;

import com.stanislav.smart.domain.market.event_stream.OrderBookRow;

public interface TradingStrategy {

    int getId();

    byte analysing(double lastPrice);

    boolean follow(OrderBookRow orderBookRow);
}
