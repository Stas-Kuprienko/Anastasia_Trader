package com.stanislav.smart.domain.market.event_stream;

import java.util.concurrent.ScheduledFuture;

public interface EventStreamListener {

    boolean isUsing();

    EventCollector getCollector();

    Runnable initStreamThread();

    ScheduledFuture<?> getScheduledFuture();

    void setScheduledFuture(ScheduledFuture<?> scheduledFuture);

    void stopStream();

    interface EventCollector {}

    interface OrderBookCollector extends EventCollector {

        OrderBookRow currentAsk();

        OrderBookRow currentBid();

        double lastAskPrice();

        double lastBidPrice();
    }
}
