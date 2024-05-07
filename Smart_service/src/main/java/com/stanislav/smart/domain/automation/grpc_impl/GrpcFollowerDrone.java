package com.stanislav.smart.domain.automation.grpc_impl;

import com.stanislav.smart.domain.automation.Drone;
import com.stanislav.smart.domain.automation.TradingStrategy;
import com.stanislav.smart.domain.entities.Security;
import com.stanislav.smart.domain.market.event_stream.EventStreamListener;

public class GrpcFollowerDrone implements Drone {

    private final Security security;
    private final TradingStrategy strategy;
    private final EventStreamListener listener;


    public GrpcFollowerDrone(Security security, TradingStrategy strategy, EventStreamListener listener) {
        this.security = security;
        this.strategy = strategy;
        this.listener = listener;
    }


    @Override
    public void launch() {
    }

    @Override
    public Security getSecurity() {
        return security;
    }

    @Override
    public TradingStrategy getStrategy() {
        return strategy;
    }

    public EventStreamListener getListener() {
        return listener;
    }

    @Override
    public void run() {
        launch();
    }

    @Override
    public void stop() {

    }
}
