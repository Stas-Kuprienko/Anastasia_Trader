package com.stanislav.smart.service.grpc_impl;

import com.stanislav.smart.service.SmartService;
import io.grpc.stub.StreamObserver;
import org.springframework.beans.factory.annotation.Autowired;
import stanislav.anastasia.trade.Smart;
import stanislav.anastasia.trade.SmartAutoTradeGrpc;
import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;

public class SmartAutoTradeGRpcImpl extends SmartAutoTradeGrpc.SmartAutoTradeImplBase {

    private final SmartService smartService;
    private final ConcurrentHashMap<Integer, AccountNode> accounts;

    @Autowired
    public SmartAutoTradeGRpcImpl(SmartService smartService) {
        this.smartService = smartService;
        this.accounts = new ConcurrentHashMap<>();
    }

    @Override
    public void subscribe(Smart.SubscribeTradeRequest request, StreamObserver<Smart.SubscribeTradeResponse> responseObserver) {
        Smart.Account account = request.getAccount();
        accounts.put(account.getUserId(), new AccountNode(account, responseObserver));
        int strategyId = request.getStrategyValue();


        //TODO just for example
        System.out.println(this.getClass().getSimpleName() + " subscribe");
        System.out.println(request.getAllFields());
        Smart.Account account1 = Smart.Account.newBuilder().setUserId(1).setClientId("client1").build();
        Smart.OrderNotification notification = Smart.OrderNotification.newBuilder()
                .setAccount(0, account1).setDateTime(LocalDateTime.now().toString()).build();
        Smart.SubscribeTradeResponse subscribeTradeResponse = Smart.SubscribeTradeResponse.newBuilder()
                .setNotification(notification).build();
        responseObserver.onNext(subscribeTradeResponse);
        responseObserver.onCompleted();
        //TODO
    }


    @Override
    public void unsubscribe(Smart.UnsubscribeRequest request, StreamObserver<Smart.UnsubscribeResponse> responseObserver) {
        //TODO
        accounts.get(request.getAccount().getUserId()).responseObserver.onCompleted();
    }

    private record AccountNode (Smart.Account account, StreamObserver<Smart.SubscribeTradeResponse> responseObserver) {}
}
