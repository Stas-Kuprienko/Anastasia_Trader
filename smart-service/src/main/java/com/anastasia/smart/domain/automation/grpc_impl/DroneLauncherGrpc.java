package com.anastasia.smart.domain.automation.grpc_impl;

import com.anastasia.smart.domain.automation.Drone;
import com.anastasia.smart.domain.automation.DroneLauncher;
import com.anastasia.smart.domain.automation.TradeStrategy;
import com.anastasia.smart.domain.automation.strategy_suppliers.StrategiesDispatcher;
import com.anastasia.smart.entities.TimeFrame;
import com.anastasia.smart.domain.trade.TradeDealingManager;
import io.grpc.stub.StreamObserver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import stanislav.anastasia.trade.Smart;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;

@Component("droneLauncher")
public class DroneLauncherGrpc implements DroneLauncher {

    private final TradeDealingManager dealingManager;
    private final StrategiesDispatcher strategiesDispatcher;
    private final ExecutorService executorService;
    private final ConcurrentHashMap<DroneCard, Drone> launched;

    @Autowired
    public DroneLauncherGrpc(TradeDealingManager dealingManager,
                             ExecutorService executorService,
                             StrategiesDispatcher strategiesDispatcher) {
        this.dealingManager = dealingManager;
        this.executorService = executorService;
        this.strategiesDispatcher = strategiesDispatcher;
        launched = new ConcurrentHashMap<>();
    }


    @Override
    public void launchDrone(Smart.SubscribeTradeRequest request, StreamObserver<Smart.SubscribeTradeResponse> response) {
        DroneCard card = new DroneCard(request.getSecurity(), request.getStrategy());
        Drone drone = launched.get(card);
        if (drone == null || ((GrpcFollowerDrone) drone).getFuture().isDone()) {
            TimeFrame.Scope timeFrame = TimeFrame.parse(request.getStrategy().getTimeFrame());
            TradeStrategy strategy = strategiesDispatcher
                    .getStrategySupplier(request.getStrategy().getName())
                    .supply(request.getSecurity(), timeFrame);
            drone = new GrpcFollowerDrone(dealingManager, strategy, request.getSecurity(), response);
            var future = executorService.submit(drone);
            ((GrpcFollowerDrone) drone).setFuture(future);
            launched.put(card, drone);
        }
        drone.addAccount(request.getAccount());
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
