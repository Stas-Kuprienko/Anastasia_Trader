package com.stanislav.event_stream.finam;

import proto.tradeapi.v1.Events;

public record OrderBookStreamListener(Collector collector) {

    public void processing(Events.Event event) {
        collector.addOrderBookEvent(event);
        System.out.println(event.getOrderBook().getAllFields());
    }
}
