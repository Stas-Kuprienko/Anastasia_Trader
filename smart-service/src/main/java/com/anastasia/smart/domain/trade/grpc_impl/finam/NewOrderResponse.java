package com.anastasia.smart.domain.trade.grpc_impl.finam;

import io.grpc.stub.StreamObserver;
import proto.tradeapi.v1.Orders;

public class NewOrderResponse implements StreamObserver<Orders.NewOrderResult> {

    private int transactionId;

    @Override
    public void onNext(Orders.NewOrderResult value) {
        transactionId = value.getTransactionId();
        //TODO get value data and save/send
    }

    @Override
    public void onError(Throwable t) {

    }

    @Override
    public void onCompleted() {

    }

    public int getTransactionId() {
        return transactionId;
    }
}
