package com.stanislav.smart_analytics.service.grpc_impl;

import io.grpc.stub.StreamObserver;
import stanislav.anastasia.trade.Smart;
import stanislav.anastasia.trade.SmartAutoTradeGrpc;

public class SmartAutoTradeGRpcService extends SmartAutoTradeGrpc.SmartAutoTradeImplBase {


    @Override
    public void subscribe(Smart.SubscribeTradeRequest request, StreamObserver<Smart.SubscribeTradeResponse> responseObserver) {
        super.subscribe(request, responseObserver);
    }

    @Override
    public void unsubscribe(Smart.UnsubscribeRequest request, StreamObserver<Smart.UnsubscribeResponse> responseObserver) {
        super.unsubscribe(request, responseObserver);
    }
}
