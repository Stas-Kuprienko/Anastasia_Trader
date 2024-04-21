package com.stanislav.event_stream.finam;

import com.stanislav.event_stream.EventStreamListener;
import grpc.tradeapi.v1.EventsGrpc;
import proto.tradeapi.v1.Events;

import java.util.concurrent.ScheduledFuture;

public class OrderBookStreamListener implements EventStreamListener {

    private final Events.SubscriptionRequest subscriptionRequest;
    private final Events.SubscriptionRequest unsubscriptionRequest;
    private final EventsGrpc.EventsStub stub;
    private final OrderBookObserver observer;
    private final FinamOrderBookCollector collector;
    private ScheduledFuture<?> scheduledFuture;


    public OrderBookStreamListener(Events.SubscriptionRequest subscription, Events.SubscriptionRequest unsubscription, EventsGrpc.EventsStub stub) {
        this.subscriptionRequest = subscription;
        this.unsubscriptionRequest = unsubscription;
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

    @Override
    public void stopStream() {
        observer.onCompleted();
        stub.getEvents(observer).onNext(unsubscriptionRequest);
        observer.onCompleted();
    }

    public class StreamObserveThread implements Runnable {

        @Override
        public void run() {
            stub.getEvents(observer).onNext(subscriptionRequest);
        }
    }
}
