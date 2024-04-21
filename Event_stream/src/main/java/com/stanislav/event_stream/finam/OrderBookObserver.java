package com.stanislav.event_stream.finam;

import com.stanislav.event_stream.EventStreamException;
import io.grpc.stub.StreamObserver;
import lombok.extern.log4j.Log4j;
import proto.tradeapi.v1.Events;

class OrderBookObserver implements StreamObserver<Events.Event> {

    private final Collector collector;

    public OrderBookObserver(Collector collector) {
        this.collector = collector;
    }


    @Override
    public void onNext(Events.Event event) {
        try {
            collector.addOrderBookEvent(event);
        } catch (EventStreamException e) {
            onCompleted();
        }
    }

    @Override
    public void onError(Throwable throwable) {

    }

    @Override
    public void onCompleted() {

    }
}
