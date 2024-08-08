package com.anastasia.smart.domain.market.event_stream.finam;

import com.anastasia.smart.exceptions.EventStreamException;
import io.grpc.stub.StreamObserver;
import proto.tradeapi.v1.Events;
import java.util.concurrent.Future;

class OrderBookObserver implements StreamObserver<Events.Event> {

    private final FinamOrderBookCollector collector;
    private Future<?> future;

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
        if (future != null && !future.isDone()) {
            future.cancel(true);
        }
        //TODO log
    }

    @Override
    public void onCompleted() {
        if (future != null && !future.isDone()) {
        future.cancel(true);
        }
    }

    public void setFuture(Future<?> future) {
        this.future = future;
    }
}