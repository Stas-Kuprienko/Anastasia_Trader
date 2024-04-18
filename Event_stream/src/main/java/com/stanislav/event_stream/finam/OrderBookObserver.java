package com.stanislav.event_stream.finam;

import io.grpc.stub.StreamObserver;
import lombok.extern.log4j.Log4j;
import proto.tradeapi.v1.Events;

class OrderBookObserver implements StreamObserver<Events.Event> {

    private final OrderBookStreamListener listener;


    public OrderBookObserver(OrderBookStreamListener listener) {
        this.listener = listener;
    }


    @Override
    public void onNext(Events.Event event) {
        listener.processing(event);
    }

    @Override
    public void onError(Throwable throwable) {

    }

    @Override
    public void onCompleted() {

    }
}
