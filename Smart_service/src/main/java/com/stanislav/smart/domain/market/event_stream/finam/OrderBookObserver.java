package com.stanislav.smart.domain.market.event_stream.finam;

import com.stanislav.smart.domain.exceptions.EventStreamException;
import io.grpc.stub.StreamObserver;
import proto.tradeapi.v1.Events;

import java.util.concurrent.ScheduledFuture;

class OrderBookObserver implements StreamObserver<Events.Event> {

    private final FinamOrderBookCollector collector;
    private ScheduledFuture<?> scheduledFuture;

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
        if (scheduledFuture != null && !scheduledFuture.isDone()) {
            scheduledFuture.cancel(true);
        }
        //TODO log
    }

    @Override
    public void onCompleted() {
        if (scheduledFuture != null && !scheduledFuture.isDone()) {
        scheduledFuture.cancel(true);
        }
    }

    public void setScheduledFuture(ScheduledFuture<?> scheduledFuture) {
        this.scheduledFuture = scheduledFuture;
    }
}