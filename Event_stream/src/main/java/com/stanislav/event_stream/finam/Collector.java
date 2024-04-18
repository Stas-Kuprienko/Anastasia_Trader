package com.stanislav.event_stream.finam;

import proto.tradeapi.v1.Events;

import java.util.concurrent.ConcurrentLinkedDeque;

public class Collector {

    private final int capacity;
    private final ConcurrentLinkedDeque<Events.OrderBookRow> asks;
    private final ConcurrentLinkedDeque<Events.OrderBookRow> bids;


    public Collector() {
        this.capacity = 10;
        this.asks = new ConcurrentLinkedDeque<>();
        this.bids = new ConcurrentLinkedDeque<>();
    }

    public Collector(int capacity) {
        this.capacity = capacity;
        this.asks = new ConcurrentLinkedDeque<>();
        this.bids = new ConcurrentLinkedDeque<>();
    }


    public Events.OrderBookRow currentAsk() {
        return asks.getLast();
    }

    public Events.OrderBookRow currentBid() {
        return bids.getLast();
    }

    public void addOrderBookEvent(Events.Event event) {
        checkSize(asks);
        checkSize(bids);
        asks.add(event.getOrderBook().getAsks(0));
        asks.add(event.getOrderBook().getBids(0));
    }

    public ConcurrentLinkedDeque<Events.OrderBookRow> getAsks() {
        return asks;
    }

    public ConcurrentLinkedDeque<Events.OrderBookRow> getBids() {
        return bids;
    }

    private void checkSize(ConcurrentLinkedDeque<Events.OrderBookRow> queue) {
        if (queue.size() > capacity) {
            queue.poll();
        }
    }
}
