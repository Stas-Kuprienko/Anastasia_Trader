package com.stanislav.event_stream.finam;

import com.stanislav.event_stream.EventStreamListener;
import grpc.tradeapi.v1.EventsGrpc;
import proto.tradeapi.v1.Events;

import java.util.concurrent.ScheduledFuture;

public class OrderBookStreamListener implements EventStreamListener {

    private final Events.SubscriptionRequest subscriptionRequest;
    private final EventsGrpc.EventsStub stub;
    private final OrderBookObserver observer;
    private final FinamOrderBookCollector collector;
    private ScheduledFuture<?> scheduledFuture;


    public OrderBookStreamListener(Events.SubscriptionRequest subscriptionRequest, EventsGrpc.EventsStub stub) {
        this.subscriptionRequest = subscriptionRequest;
        this.stub = stub;
        this.collector = new FinamOrderBookCollector();
        this.observer = new OrderBookObserver(collector);
    }


    @Override
    public FinamOrderBookCollector getCollector() {
        return collector;
    }

    @Override
    public StreamObserveThread initStreamThread() {
        return new StreamObserveThread();
    }

    @Override
    public ScheduledFuture<?> getScheduledFuture() {
        return scheduledFuture;
    }

    @Override
    public void setScheduledFuture(ScheduledFuture<?> scheduledFuture) {
        this.scheduledFuture = scheduledFuture;
    }

    public class StreamObserveThread implements Runnable {

        @Override
        public void run() {
            stub.getEvents(observer).onNext(subscriptionRequest);
        }
    }
}
