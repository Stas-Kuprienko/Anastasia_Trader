package com.stanislav.smart_analytics.event_stream.finam;

import com.stanislav.smart_analytics.event_stream.EventStreamException;
import com.stanislav.smart_analytics.event_stream.service.EventStreamListener;
import com.stanislav.smart_analytics.event_stream.service.OrderBookRow;
import proto.tradeapi.v1.Events;

import java.util.concurrent.ConcurrentLinkedDeque;

public class FinamOrderBookCollector implements
        EventStreamListener.EventCollector, EventStreamListener.OrderBookCollector<Events.Event, Events.OrderBookRow> {

    private static final byte DEFAULT_CAPACITY = 10;
    private final int capacity;
    private final ConcurrentLinkedDeque<Events.OrderBookRow> asks;
    private final ConcurrentLinkedDeque<Events.OrderBookRow> bids;


    public FinamOrderBookCollector() {
        this.capacity = DEFAULT_CAPACITY;
        this.asks = new ConcurrentLinkedDeque<>();
        this.bids = new ConcurrentLinkedDeque<>();
    }

    public FinamOrderBookCollector(int capacity) {
        this.capacity = capacity;
        this.asks = new ConcurrentLinkedDeque<>();
        this.bids = new ConcurrentLinkedDeque<>();
    }


    @Override
    public OrderBookRow currentAsk() {
        return new FinamOrderBookRowProxy(asks.getLast());
    }

    @Override
    public OrderBookRow currentBid() {
        return new FinamOrderBookRowProxy(bids.getLast());
    }

    @Override
    public void addOrderBookEvent(Events.Event event) throws EventStreamException {
        if (event.hasOrderBook()) {
            checkSize(asks);
            checkSize(bids);
            asks.add(event.getOrderBook().getAsks(0));
            bids.add(event.getOrderBook().getBids(0));
        } else {
            System.out.println(event.getResponse());
            if (!event.getResponse().getSuccess()) {
                //TODO loggers
                event.getResponse().getErrorsList();
                throw new EventStreamException();
            }
        }
    }

    @Override
    public ConcurrentLinkedDeque<Events.OrderBookRow> getAsks() {
        return asks;
    }

    @Override
    public ConcurrentLinkedDeque<Events.OrderBookRow> getBids() {
        return bids;
    }

    private void checkSize(ConcurrentLinkedDeque<Events.OrderBookRow> queue) {
        if (queue.size() > capacity) {
            queue.poll();
        }
    }
}
