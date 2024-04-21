package com.stanislav.event_stream;

import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ScheduledFuture;

public interface EventStreamListener {

    EventCollector getCollector();

    Runnable initStreamThread();

    ScheduledFuture<?> getScheduledFuture();

    void setScheduledFuture(ScheduledFuture<?> scheduledFuture);

    void stopStream();

    interface EventCollector {}

    interface OrderBookCollector <E, R> extends EventCollector {

        OrderBookRow currentAsk();

        OrderBookRow currentBid();

        void addOrderBookEvent(E event) throws EventStreamException;

        ConcurrentLinkedDeque<R> getAsks();

        ConcurrentLinkedDeque<R> getBids();
    }
}
