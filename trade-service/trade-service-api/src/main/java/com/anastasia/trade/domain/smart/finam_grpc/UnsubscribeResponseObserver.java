package com.anastasia.trade.domain.smart.finam_grpc;

import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import com.anastasia.trade.Smart;

@Slf4j
public class UnsubscribeResponseObserver implements StreamObserver<Smart.UnsubscribeResponse> {

    @Override
    public void onNext(Smart.UnsubscribeResponse value) {
        if (value.getIsUnsubscribe()) {

        }
    }

    @Override
    public void onError(Throwable t) {
        log.error(t.getMessage());
    }

    @Override
    public void onCompleted() {

    }
}
