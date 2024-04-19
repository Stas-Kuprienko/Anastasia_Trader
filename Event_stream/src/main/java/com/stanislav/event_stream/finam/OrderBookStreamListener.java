package com.stanislav.event_stream.finam;

import grpc.tradeapi.v1.EventsGrpc;
import io.grpc.stub.StreamObserver;
import proto.tradeapi.v1.Events;

import java.util.concurrent.Callable;

public class OrderBookStreamListener implements Callable<Collector> {

    private final Events.SubscriptionRequest subscriptionRequest;
    private final EventsGrpc.EventsStub stub;
    private final Collector collector;

    public OrderBookStreamListener(Events.SubscriptionRequest subscriptionRequest, EventsGrpc.EventsStub stub) {
        this.subscriptionRequest = subscriptionRequest;
        this.stub = stub;
        this.collector = new Collector();
    }


    @Override
    public Collector call() throws Exception {
        stub.getEvents(new OrderBookObserver(collector)).onNext(subscriptionRequest);
        return collector;
    }
}
