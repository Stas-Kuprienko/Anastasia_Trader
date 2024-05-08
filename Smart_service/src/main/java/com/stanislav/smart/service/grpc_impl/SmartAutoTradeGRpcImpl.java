package com.stanislav.smart.service.grpc_impl;

import com.stanislav.smart.service.SmartService;
import io.grpc.stub.StreamObserver;
import stanislav.anastasia.trade.Smart;
import stanislav.anastasia.trade.SmartAutoTradeGrpc;

public class SmartAutoTradeGRpcImpl extends SmartAutoTradeGrpc.SmartAutoTradeImplBase {

    private final SmartService smartService;


    public SmartAutoTradeGRpcImpl(SmartService smartService) {
        this.smartService = smartService;
    }


    @Override
    public void subscribe(Smart.SubscribeTradeRequest request, StreamObserver<Smart.SubscribeTradeResponse> responseObserver) {
        String strategy = request.getStrategy().getPayloadCase().name();


        //TODO just for example
        System.out.println(this.getClass().getSimpleName() + " subscribe");
        System.out.println(request.getAllFields());
        Smart.OrderNotification notification = Smart.OrderNotification.newBuilder()
                .setStrategy(request.getStrategy()).setSecurity(request.getSecurity()).setPrice(0).build();
        Smart.SubscribeTradeResponse subscribeTradeResponse = Smart.SubscribeTradeResponse.newBuilder()
                .setNotification(notification).build();
        responseObserver.onNext(subscribeTradeResponse);
        responseObserver.onCompleted();
        //TODO
    }


    @Override
    public void unsubscribe(Smart.UnsubscribeRequest request, StreamObserver<Smart.UnsubscribeResponse> responseObserver) {
        //TODO
    }
}
