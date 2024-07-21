package com.stanislav.smart.domain.market.event_stream.finam;

import com.stanislav.smart.domain.market.event_stream.OrderBookStreamListener;
import grpc.tradeapi.v1.EventsGrpc;
import proto.tradeapi.v1.Events;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Future;

public class OrderBookRpcThreadListener implements OrderBookStreamListener {

    private final Set<Object> consumers;
    private final Events.SubscriptionRequest subscribeRequest;
    private final Events.SubscriptionRequest unsubscribeRequest;
    private final EventsGrpc.EventsStub stub;
    private final OrderBookObserver observer;
    private final FinamOrderBookCollector collector;
    private Future<?> future;


    public OrderBookRpcThreadListener(Events.SubscriptionRequest subscribe, Events.SubscriptionRequest unsubscribe, EventsGrpc.EventsStub stub) {
        this.subscribeRequest = subscribe;
        this.unsubscribeRequest = unsubscribe;
        this.stub = stub;
        this.collector = new FinamOrderBookCollector();
        this.observer = new OrderBookObserver(collector);
        consumers = new HashSet<>();
    }

    @Override
    public void addConsumer(Object o) {
        consumers.add(o);
    }

    @Override
    public void removeConsumer(Object o) {
        consumers.remove(o);
    }

    @Override
    public boolean isUseless() {
        return consumers.isEmpty();
    }

    @Override
    public OrderBookCollector orderBookCollector() {
        return collector;
    }

    @Override
    public boolean stop() {
        if (isUseless()) {
            stub.getEvents(observer).onCompleted();
            stub.getEvents(observer).onNext(unsubscribeRequest);
            stub.getEvents(observer).onCompleted();
            if (!future.isDone()) {
                future.cancel(true);
            }
            return true;
        } else {
            return false;
        }
    }

    public StreamObserveThread initStreamThread() {
        return new StreamObserveThread();
    }

    public Future<?> getFuture() {
        return future;
    }

    public void setFuture(Future<?> future) {
        this.future = future;
        observer.setFuture(future);
    }


    public class StreamObserveThread implements Runnable {

        @Override
        public void run() {
            stub.getEvents(observer).onNext(subscribeRequest);
        }
    }
}
