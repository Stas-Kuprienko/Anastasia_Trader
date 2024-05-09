package com.stanislav.smart.domain.controller.grpc_impl;

import com.stanislav.smart.domain.analysis.technical.AnalysisAideSupplier;
import com.stanislav.smart.domain.automation.Drone;
import com.stanislav.smart.domain.automation.TradingStrategy;
import com.stanislav.smart.domain.automation.grpc_impl.GrpcFollowerDrone;
import com.stanislav.smart.domain.automation.grpc_impl.StrategiesGrpcDispatcher;
import com.stanislav.smart.domain.controller.DroneLauncher;
import com.stanislav.smart.domain.entities.Board;
import com.stanislav.smart.domain.entities.Security;
import com.stanislav.smart.domain.market.event_stream.EventStream;
import io.grpc.stub.StreamObserver;
import stanislav.anastasia.trade.Smart;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class GRpcDroneLauncher
        implements DroneLauncher <Smart.SubscribeTradeRequest, StreamObserver<Smart.SubscribeTradeResponse>> {

    private final StrategiesGrpcDispatcher strategiesDispatcher;
    private final ScheduledExecutorService scheduledExecutor;
    private final ConcurrentHashMap<Security, Drone> launched;


    public GRpcDroneLauncher(EventStream eventStream,
                             AnalysisAideSupplier analysisAideSupplier,
                             ScheduledExecutorService scheduledExecutor) {
        this.scheduledExecutor = scheduledExecutor;
        this.strategiesDispatcher = new StrategiesGrpcDispatcher(analysisAideSupplier, eventStream);
        launched = new ConcurrentHashMap<>();
    }


    @Override
    public Drone launchDrone(Smart.SubscribeTradeRequest request, StreamObserver<Smart.SubscribeTradeResponse> responseObserver) {

        Security security = new Security(request.getSecurity().getTicker(), Board.valueOf(request.getSecurity().getBoard()));
        Drone drone = launched.get(security);
        if (drone == null) {
            TradingStrategy strategy = strategiesDispatcher.recognizeStrategy(request);
            drone = new GrpcFollowerDrone(security, strategy, request, responseObserver);
            launched.put(security, drone);

        }
        var scheduledFuture = scheduledExecutor.schedule(drone, 1, TimeUnit.SECONDS);
        return drone;
    }
}
