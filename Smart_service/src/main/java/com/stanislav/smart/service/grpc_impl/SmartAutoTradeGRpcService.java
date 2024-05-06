package com.stanislav.smart.service.grpc_impl;

import com.stanislav.smart.service.SmartAutoTradeService;
import io.grpc.stub.StreamObserver;
import stanislav.anastasia.trade.Smart;
import stanislav.anastasia.trade.SmartAutoTradeGrpc;
import java.time.LocalDateTime;

public class SmartAutoTradeGRpcService extends SmartAutoTradeGrpc.SmartAutoTradeImplBase implements SmartAutoTradeService {


    @Override
    public void subscribe(Smart.SubscribeTradeRequest request, StreamObserver<Smart.SubscribeTradeResponse> responseObserver) {

        //TODO just for example
        System.out.println(this.getClass().getSimpleName() + " subscribe");
        System.out.println(request.getAllFields());
        Smart.Account account = Smart.Account.newBuilder().setClientId("client1").build();
        Smart.OrderNotification notification = Smart.OrderNotification.newBuilder()
                .setAccount(account).setDateTime(LocalDateTime.now().toString()).build();
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
