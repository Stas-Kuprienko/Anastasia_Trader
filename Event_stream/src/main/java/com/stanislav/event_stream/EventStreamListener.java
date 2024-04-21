package com.stanislav.event_stream;

import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ScheduledFuture;

public interface EventStreamListener {

    Collector getCollector();

    Runnable initStreamThread();

    ScheduledFuture<?> getScheduledFuture();

    void setScheduledFuture(ScheduledFuture<?> scheduledFuture);

    interface Collector {}

    interface OrderBookCollector <E, R> extends Collector {

        R currentAsk();

        R currentBid();

        void addOrderBookEvent(E event) throws EventStreamException;

        ConcurrentLinkedDeque<R> getAsks();

        ConcurrentLinkedDeque<R> getBids();
    }
}
