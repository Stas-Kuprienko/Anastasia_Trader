package com.stanislav.smart.domain.controller.grpc_impl;

import com.stanislav.smart.domain.automation.Drone;
import com.stanislav.smart.domain.automation.TradingStrategy;
import com.stanislav.smart.domain.automation.grpc_impl.GrpcFollowerDrone;
import com.stanislav.smart.domain.automation.grpc_impl.StrategiesDispatcher;
import com.stanislav.smart.domain.controller.DroneLauncher;
import com.stanislav.smart.domain.entities.TimeFrame;
import io.grpc.stub.StreamObserver;
import stanislav.anastasia.trade.Smart;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class GRpcDroneLauncher implements DroneLauncher {

    private final StrategiesDispatcher strategiesDispatcher;
    private final ScheduledExecutorService scheduledExecutor;
    private final ConcurrentHashMap<Smart.Security, Drone> launched;


    public GRpcDroneLauncher(ScheduledExecutorService scheduledExecutor, StrategiesDispatcher strategiesDispatcher) {
        this.scheduledExecutor = scheduledExecutor;
        this.strategiesDispatcher = strategiesDispatcher;
        launched = new ConcurrentHashMap<>();
    }


    @Override
    public Drone launchDrone(Smart.SubscribeTradeRequest request, StreamObserver<Smart.SubscribeTradeResponse> response) {

        Drone drone = launched.get(request.getSecurity());
        if (drone == null) {
            TimeFrame.Scope timeFrame = TimeFrame.parse(request.getStrategy().getTimeFrame());
            TradingStrategy strategy = strategiesDispatcher
                    .getStrategySupplier(request.getStrategy().getName())
                    .supply(request.getSecurity(), timeFrame);
            drone = new GrpcFollowerDrone(request.getSecurity(), strategy, request, response);
            launched.put(request.getSecurity(), drone);

        }
        var scheduledFuture = scheduledExecutor.schedule(drone, 1, TimeUnit.SECONDS);
        drone.setScheduledFuture(scheduledFuture);
        return drone;
    }

    @Override
    public boolean stopDrone(Smart.Security security) {
        return launched.get(security).stop();
    }
}
