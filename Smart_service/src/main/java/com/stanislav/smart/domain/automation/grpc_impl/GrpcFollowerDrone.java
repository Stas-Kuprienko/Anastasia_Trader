package com.stanislav.smart.domain.automation.grpc_impl;

import com.stanislav.smart.domain.automation.Drone;
import com.stanislav.smart.domain.automation.TradingStrategy;
import com.stanislav.smart.domain.entities.Security;
import io.grpc.stub.StreamObserver;
import stanislav.anastasia.trade.Smart;

public class GrpcFollowerDrone implements Drone {

    private final Security security;
    private final TradingStrategy strategy;
    private final Smart.SubscribeTradeRequest request;
    private final StreamObserver<Smart.SubscribeTradeResponse> responseObserver;


    public GrpcFollowerDrone(Security security, TradingStrategy strategy,
                             Smart.SubscribeTradeRequest request,
                             StreamObserver<Smart.SubscribeTradeResponse> responseObserver) {
        this.security = security;
        this.strategy = strategy;
        this.request = request;
        this.responseObserver = responseObserver;
    }


    @Override
    public void launch() {
        boolean isOrder;
        byte topic = strategy.analysing();
        if (topic < 1) {
            isOrder = strategy.observe();
            if (isOrder) {
                Smart.OrderNotification notification = Smart.OrderNotification.newBuilder()
                        .setStrategy(request.getStrategy()).setSecurity(request.getSecurity()).setPrice(0).build();

                Smart.SubscribeTradeResponse subscribeTradeResponse = Smart.SubscribeTradeResponse.newBuilder()
                        .setNotification(notification).build();

                responseObserver.onNext(subscribeTradeResponse);
            }
        }
    }

    @Override
    public Security getSecurity() {
        return security;
    }

    @Override
    public TradingStrategy getStrategy() {
        return strategy;
    }

    @Override
    public void run() {
        launch();
    }

    @Override
    public void stop() {

    }
}
