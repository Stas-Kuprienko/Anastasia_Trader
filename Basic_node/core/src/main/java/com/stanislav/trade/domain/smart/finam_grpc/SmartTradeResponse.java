package com.stanislav.trade.domain.smart.finam_grpc;

import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import stanislav.anastasia.trade.Smart;

@Slf4j
public class SmartTradeResponse implements StreamObserver<Smart.SubscribeTradeResponse> {

    @Override
    public void onNext(Smart.SubscribeTradeResponse response) {
        Smart.OrderNotification notification = response.getNotification();

        //TODO test
        System.out.println(this.getClass().getSimpleName() + " onNext");
        System.out.println(notification);
    }

    @Override
    public void onError(Throwable t) {
        log.error(t.getMessage());
    }

    @Override
    public void onCompleted() {
        System.out.println("completed");
    }
}
