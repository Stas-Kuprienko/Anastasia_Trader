package com.stanislav.smart.domain.controller.grpc_impl;

import com.stanislav.smart.domain.controller.DroneLauncher;
import io.grpc.stub.StreamObserver;
import stanislav.anastasia.trade.Smart;
import stanislav.anastasia.trade.SmartAutoTradeGrpc;

public class SmartAutoTradeGRpcImpl extends SmartAutoTradeGrpc.SmartAutoTradeImplBase {

    private final DroneLauncher<Smart.SubscribeTradeRequest, StreamObserver<Smart.SubscribeTradeResponse>> droneLauncher;


    @SuppressWarnings("unchecked")
    public SmartAutoTradeGRpcImpl(DroneLauncher<?, ?> droneLauncher) {
            this.droneLauncher = (DroneLauncher<Smart.SubscribeTradeRequest, StreamObserver<Smart.SubscribeTradeResponse>>) droneLauncher;
    }


    @Override
    public void subscribe(Smart.SubscribeTradeRequest request, StreamObserver<Smart.SubscribeTradeResponse> responseObserver) {
        droneLauncher.launchDrone(request, responseObserver);
    }


    @Override
    public void unsubscribe(Smart.UnsubscribeRequest request, StreamObserver<Smart.UnsubscribeResponse> responseObserver) {
        //TODO
    }
}
