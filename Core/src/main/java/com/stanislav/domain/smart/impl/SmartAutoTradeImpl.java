package com.stanislav.domain.smart.impl;

import com.stanislav.domain.smart.SmartAutoTradeService;
import com.stanislav.domain.smart.service.Authentication;
import com.stanislav.domain.smart.service.GRpcConnection;
import com.stanislav.domain.smart.strategy.TradeStrategy;
import com.stanislav.entities.Board;
import io.grpc.stub.StreamObserver;
import stanislav.anastasia.trade.Smart;
import stanislav.anastasia.trade.SmartAutoTradeGrpc;
import stanislav.anastasia.trade.Strategies;

public class SmartAutoTradeImpl implements SmartAutoTradeService {

    private final SmartAutoTradeGrpc.SmartAutoTradeStub stub;


    public SmartAutoTradeImpl(String appId, String secretKey, GRpcConnection connection) {
        String token = Authentication.generateToken(appId, secretKey);
        this.stub = SmartAutoTradeGrpc.newStub(connection.getChannel()).
                withCallCredentials(Authentication.xApiKeyAuthorization(token));
    }

    @Override
    public void subscribe(String ticker, Board board, TradeStrategy strategy) {

        Strategies.Strategy str = Strategies.Strategy.newBuilder()
                .setSimpleMovingAverageCrossing(
                        Strategies.SimpleMovingAverageCrossing.newBuilder()
                                .setTimeFrame(Strategies.TimeFrame.H1)
                                .setPeriod(5).build()
                ).build();

        Smart.Security security = Smart.Security.newBuilder()
                .setTicker(ticker)
                .setBoard(board.toString()).build();

        Smart.SubscribeTradeRequest request = Smart.SubscribeTradeRequest.newBuilder()
                .setSecurity(security)
                .setStrategy(str)
                .build();
        //TODO !!!!!!!!!!!!!!!!!!!!!!!
        SmartTradeResponse response = new SmartTradeResponse();
        stub.subscribe(request, response);


    }


    public static class SmartTradeResponse implements StreamObserver<Smart.SubscribeTradeResponse> {

        @Override
        public void onNext(Smart.SubscribeTradeResponse response) {
            Smart.OrderNotification notification = response.getNotification();

            //TODO test
            System.out.println(this.getClass().getSimpleName() + " onNext");
            System.out.println(notification);
        }

        @Override
        public void onError(Throwable t) {

        }

        @Override
        public void onCompleted() {
            System.out.println("completed");
        }
    }
}