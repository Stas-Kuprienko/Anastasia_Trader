package com.stanislav.smart.service.grpc_impl;

import com.stanislav.smart.domain.automation.drones.Drone;
import com.stanislav.smart.domain.automation.strategies.StrategyManager;
import com.stanislav.smart.domain.automation.strategies.TradingStrategy;
import com.stanislav.smart.service.SmartService;

import javax.annotation.PreDestroy;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class SmartServiceGrpcImpl implements SmartService {

    private final ScheduledExecutorService scheduledExecutor;
    private final ConcurrentHashMap<String, Drone> drones;
    private final StrategyManager strategyManager;


    public SmartServiceGrpcImpl(int threadPoolSize, StrategyManager strategyManager) {
        this.scheduledExecutor = Executors.newScheduledThreadPool(threadPoolSize);
        this.drones = new ConcurrentHashMap<>();
        this.strategyManager = strategyManager;
    }


    public TradingStrategy getStrategy(int id) {
        return null;
    }

    @Override
    public ScheduledExecutorService getScheduledExecutor() {
        return scheduledExecutor;
    }

    @PreDestroy
    public void close() {
        scheduledExecutor.shutdown();
    }
}
