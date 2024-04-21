package com.stanislav.event_stream.finam;

import com.stanislav.event_stream.EventStreamException;
import com.stanislav.event_stream.EventStreamListener;
import proto.tradeapi.v1.Events;

import java.util.concurrent.ConcurrentLinkedDeque;

public class FinamOrderBookCollector implements
        EventStreamListener.Collector, EventStreamListener.OrderBookCollector<Events.Event, Events.OrderBookRow> {

    private final int capacity;
    private final ConcurrentLinkedDeque<Events.OrderBookRow> asks;
    private final ConcurrentLinkedDeque<Events.OrderBookRow> bids;


    public FinamOrderBookCollector() {
        this.capacity = 10;
        this.asks = new ConcurrentLinkedDeque<>();
        this.bids = new ConcurrentLinkedDeque<>();
    }

    public FinamOrderBookCollector(int capacity) {
        this.capacity = capacity;
        this.asks = new ConcurrentLinkedDeque<>();
        this.bids = new ConcurrentLinkedDeque<>();
    }


    @Override
    public Events.OrderBookRow currentAsk() {
        return asks.getLast();
    }

    @Override
    public Events.OrderBookRow currentBid() {
        return bids.getLast();
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
