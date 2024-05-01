package com.stanislav.smart.domain.event_stream.finam;

import com.stanislav.smart.domain.event_stream.EventStreamListener;
import grpc.tradeapi.v1.EventsGrpc;
import proto.tradeapi.v1.Events;

import java.util.concurrent.ScheduledFuture;

public class OrderBookStreamListener implements EventStreamListener {

    private final Events.SubscriptionRequest subscribeRequest;
    private final Events.SubscriptionRequest unsubscribeRequest;
    private final EventsGrpc.EventsStub stub;
    private final OrderBookObserver observer;
    private final FinamOrderBookCollector collector;
    private ScheduledFuture<?> scheduledFuture;


    public OrderBookStreamListener(Events.SubscriptionRequest subscribe, Events.SubscriptionRequest unsubscribe, EventsGrpc.EventsStub stub) {
        this.subscribeRequest = subscribe;
        this.unsubscribeRequest = unsubscribe;
        this.stub = stub;
        this.collector = new FinamOrderBookCollector();
        this.observer = new OrderBookObserver(collector);
    }


    @Override
    public boolean isUsing() {
        //TODO
        return false;
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
        stub.getEvents(observer).onCompleted();
        stub.getEvents(observer).onNext(unsubscribeRequest);
        stub.getEvents(observer).onCompleted();
    }

    public class StreamObserveThread implements Runnable {

        @Override
        public void run() {
            stub.getEvents(observer).onNext(subscribeRequest);
        }
    }
}
