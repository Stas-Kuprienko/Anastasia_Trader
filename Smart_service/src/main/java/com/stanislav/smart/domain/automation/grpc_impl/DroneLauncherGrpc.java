package com.stanislav.smart.domain.automation.grpc_impl;

import com.stanislav.smart.domain.automation.Drone;
import com.stanislav.smart.domain.automation.DroneLauncher;
import com.stanislav.smart.domain.automation.TradingStrategy;
import com.stanislav.smart.domain.automation.strategy_boxes.StrategiesDispatcher;
import com.stanislav.smart.domain.entities.TimeFrame;
import io.grpc.stub.StreamObserver;
import stanislav.anastasia.trade.Smart;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class DroneLauncherGrpc implements DroneLauncher {

    private final StrategiesDispatcher strategiesDispatcher;
    private final ScheduledExecutorService scheduledExecutor;
    private final ConcurrentHashMap<DroneCard, Drone> launched;


    public DroneLauncherGrpc(ScheduledExecutorService scheduledExecutor, StrategiesDispatcher strategiesDispatcher) {
        this.scheduledExecutor = scheduledExecutor;
        this.strategiesDispatcher = strategiesDispatcher;
        launched = new ConcurrentHashMap<>();
    }


    @Override
    public Drone launchDrone(Smart.SubscribeTradeRequest request, StreamObserver<Smart.SubscribeTradeResponse> response) {
        DroneCard card = new DroneCard(request.getSecurity(), request.getStrategy());
        Drone drone = launched.get(card);
        if (drone == null || drone.getScheduledFuture().isDone()) {
            TimeFrame.Scope timeFrame = TimeFrame.parse(request.getStrategy().getTimeFrame());
            TradingStrategy strategy = strategiesDispatcher
                    .getStrategySupplier(request.getStrategy().getName())
                    .supply(request.getSecurity(), timeFrame);
            drone = new GrpcFollowerDrone(strategy, request, response);
            var scheduledFuture = scheduledExecutor.schedule(drone, 1, TimeUnit.SECONDS);
            drone.setScheduledFuture(scheduledFuture);
            launched.put(card, drone);
        }
        drone.addAccount(request.getAccount());
        return drone;
    }

    @Override
    public void removeAccount(Smart.UnsubscribeRequest unsubscribeRequest) {
        DroneCard card = new DroneCard(unsubscribeRequest.getSecurity(), unsubscribeRequest.getStrategy());
        Drone drone = launched.get(card);
        if (drone != null) {
            drone.removeAccount(unsubscribeRequest.getAccount());
            if (drone.isUseless()) {
                drone.stop();
                launched.remove(card);
            }
        }
    }

    @Override
    public boolean stopDrone(Smart.Security security, Smart.Strategy strategy) {
        DroneCard droneCard = new DroneCard(security, strategy);
        Drone drone = launched.get(droneCard);
        if (drone != null) {
            return drone.stop();
        }
        return false;
    }


    record DroneCard(Smart.Security security, Smart.Strategy strategy) {}
}
