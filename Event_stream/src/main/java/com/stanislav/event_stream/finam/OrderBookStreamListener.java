package com.stanislav.event_stream.finam;

import grpc.tradeapi.v1.EventsGrpc;
import io.grpc.stub.StreamObserver;
import proto.tradeapi.v1.Events;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledFuture;

public class OrderBookStreamListener {

    private final Events.SubscriptionRequest subscriptionRequest;
    private final EventsGrpc.EventsStub stub;
    private final OrderBookObserver observer;
    private final Collector collector;
    private ScheduledFuture<?> scheduledFuture;

    public OrderBookStreamListener(Events.SubscriptionRequest subscriptionRequest, EventsGrpc.EventsStub stub) {
        this.subscriptionRequest = subscriptionRequest;
        this.stub = stub;
        this.collector = new Collector();
        this.observer = new OrderBookObserver(collector);
    }

    public Collector getCollector() {
        return collector;
    }

    public StreamObserveThread initStreamObserveThread() {
        return new StreamObserveThread();
    }

    public ScheduledFuture<?> getScheduledFuture() {
        return scheduledFuture;
    }

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
