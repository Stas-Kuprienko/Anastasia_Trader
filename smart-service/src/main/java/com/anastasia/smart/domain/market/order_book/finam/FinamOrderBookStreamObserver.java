package com.anastasia.smart.domain.market.order_book.finam;

import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import proto.tradeapi.v1.Events;
import java.util.Arrays;

@Slf4j
public record FinamOrderBookStreamObserver(FinamOrderBookEntry entry) implements StreamObserver<Events.Event> {


    @Override
    public void onNext(Events.Event event) {
        entry.setOrderBook(event);
    }

    @Override
    public void onError(Throwable t) {
        log.error(Arrays.toString(t.getStackTrace()));
        entry.deactivate();
    }

    @Override
    public void onCompleted() {
        entry.deactivate();
    }
}