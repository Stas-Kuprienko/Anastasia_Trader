package com.stanislav.smart.domain.automation.grpc_impl;

import com.google.protobuf.Empty;
import com.stanislav.smart.domain.automation.strategy_boxes.StrategiesDispatcher;
import com.stanislav.smart.domain.automation.DroneLauncher;
import io.grpc.stub.StreamObserver;
import stanislav.anastasia.trade.Smart;
import stanislav.anastasia.trade.SmartAutoTradeGrpc;
import java.util.NoSuchElementException;

public class SmartAutoTradeGRpcImpl extends SmartAutoTradeGrpc.SmartAutoTradeImplBase {

    private final StrategiesDispatcher dispatcher;
    private final DroneLauncher droneLauncher;


    public SmartAutoTradeGRpcImpl(StrategiesDispatcher dispatcher, DroneLauncher droneLauncher) {
        this.dispatcher = dispatcher;
        this.droneLauncher = droneLauncher;
    }


    @Override
    public void subscribe(Smart.SubscribeTradeRequest request, StreamObserver<Smart.SubscribeTradeResponse> responseObserver) {
        droneLauncher.launchDrone(request, responseObserver);
    }

    @Override
    public void unsubscribe(Smart.UnsubscribeRequest request, StreamObserver<Smart.UnsubscribeResponse> responseObserver) {
        //TODO !!!
        droneLauncher.stopDrone(request.getSecurity());
    }

    @Override
    public void getStrategies(Empty request, StreamObserver<Smart.StrategiesList> responseObserver) {
        var strategies = dispatcher.strategiesNameList();
        if (strategies == null || strategies.isEmpty()) {
            responseObserver.onError(new NoSuchElementException("strategies list is null or empty!"));
            responseObserver.onCompleted();
        } else {
            Smart.StrategiesList list = Smart.StrategiesList.newBuilder()
                    .addAllItem(strategies)
                    .build();
            responseObserver.onNext(list);
            responseObserver.onCompleted();
        }
    }
}