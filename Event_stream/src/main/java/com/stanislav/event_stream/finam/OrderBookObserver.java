package com.stanislav.event_stream.finam;

import io.grpc.stub.StreamObserver;
import proto.tradeapi.v1.Events;

class OrderBookObserver implements StreamObserver<Events.Event> {


    @Override
    public void onNext(Events.Event event) {
        System.out.println("event: - ");
        System.out.println(event.getAllFields().toString());
    }

    @Override
    public void onError(Throwable throwable) {

    }

    @Override
    public void onCompleted() {

    }
}
