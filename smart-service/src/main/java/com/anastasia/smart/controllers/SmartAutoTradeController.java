package com.anastasia.smart.controllers;

import com.anastasia.trade.Smart;
import com.anastasia.trade.SmartAutoTradeGrpc;
import com.anastasia.smart.configuration.grpc_impl.security.ServerSecurityInterceptor;
import com.google.protobuf.Empty;
import com.anastasia.smart.domain.automation.strategy_suppliers.StrategiesDispatcher;
import com.anastasia.smart.domain.automation.DroneLauncher;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.NoSuchElementException;

@GrpcService(interceptors = ServerSecurityInterceptor.class)
public class SmartAutoTradeController extends SmartAutoTradeGrpc.SmartAutoTradeImplBase {

    private final StrategiesDispatcher dispatcher;
    private final DroneLauncher droneLauncher;

    @Autowired
    public SmartAutoTradeController(StrategiesDispatcher dispatcher, DroneLauncher droneLauncher) {
        this.dispatcher = dispatcher;
        this.droneLauncher = droneLauncher;
    }


    @Override
    public void subscribe(Smart.SubscribeTradeRequest request, StreamObserver<Smart.SubscribeTradeResponse> responseObserver) {
        droneLauncher.launchDrone(request, responseObserver);
    }

    @Override
    public void unsubscribe(Smart.UnsubscribeRequest request, StreamObserver<Smart.UnsubscribeResponse> responseObserver) {
        droneLauncher.removeAccount(request);
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