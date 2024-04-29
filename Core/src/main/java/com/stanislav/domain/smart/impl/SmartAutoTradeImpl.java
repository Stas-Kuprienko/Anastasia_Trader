package com.stanislav.domain.smart.impl;

import com.stanislav.domain.smart.SmartAutoTradeService;
import com.stanislav.domain.smart.service.GRpcConnection;
import com.stanislav.domain.smart.strategy.TradeStrategy;
import com.stanislav.entities.user.Account;
import io.grpc.stub.StreamObserver;
import stanislav.anastasia.trade.Smart;
import stanislav.anastasia.trade.SmartAutoTradeGrpc;

public class SmartAutoTradeImpl implements SmartAutoTradeService {

    private final SmartAutoTradeGrpc.SmartAutoTradeStub stub;


    public SmartAutoTradeImpl(String token, GRpcConnection connection) {
        this.stub = SmartAutoTradeGrpc.newStub(connection.getChannel()).
                withCallCredentials(connection.bearerAuthorization(token));
    }

    @Override
    public void subscribe(Account account, String security, double price, int quantity, TradeStrategy strategy) {

        Smart.Account requestAccount = Smart.Account.newBuilder()
                .setClientId(account.getClientId())
                .setToken(account.getToken())
                .build();

        Smart.SubscribeTradeRequest request = Smart.SubscribeTradeRequest.newBuilder()
                .setAccount(requestAccount)
                .setSecurity(security)
                .setPrice(price)
                .setQuantity(quantity)
                .setTimeFrame(Smart.TimeFrame.H1)
                .setStrategy(Smart.Strategy.MOVING_AVERAGE)
                .build();
        //TODO !!!!!!!!!!!!!!!!!!!!!!!
        SmartTradeResponse response = new SmartTradeResponse();
        stub.subscribe(request, response);


    }


    public static class SmartTradeResponse implements StreamObserver<Smart.SubscribeTradeResponse> {

        @Override
        public void onNext(Smart.SubscribeTradeResponse response) {
            Smart.OrderNotification notification = response.getNotification();
        }

        @Override
        public void onError(Throwable t) {

        }

        @Override
        public void onCompleted() {

        }
    }
}