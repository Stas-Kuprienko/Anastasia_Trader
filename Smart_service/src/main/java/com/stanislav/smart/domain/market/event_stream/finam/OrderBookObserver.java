package com.stanislav.smart.domain.market.event_stream.finam;

import com.stanislav.smart.domain.market.event_stream.EventStreamException;
import io.grpc.stub.StreamObserver;
import proto.tradeapi.v1.Events;

class OrderBookObserver implements StreamObserver<Events.Event> {

    private final FinamOrderBookCollector collector;

    public OrderBookObserver(FinamOrderBookCollector collector) {
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
