package com.stanislav.smart.domain.controller.impl;

import com.stanislav.smart.domain.automation.Drone;
import com.stanislav.smart.domain.automation.TradingStrategy;
import com.stanislav.smart.domain.automation.grpc_impl.GrpcFollowerDrone;
import com.stanislav.smart.domain.controller.SmartService;
import com.stanislav.smart.domain.entities.Security;
import com.stanislav.smart.domain.market.event_stream.EventStream;
import com.stanislav.smart.domain.market.event_stream.EventStreamListener;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;

public class MySmartService implements SmartService {

    private final ConcurrentHashMap<String, Drone> drones;
    private final ScheduledExecutorService scheduledExecutor;
    private final EventStream eventStream;

    public MySmartService(ScheduledExecutorService scheduledExecutor, EventStream eventStream) {
        this.scheduledExecutor = scheduledExecutor;
        this.eventStream = eventStream;
        this.drones = new ConcurrentHashMap<>();
    }

    public void launchDrone(Security security, TradingStrategy strategy) {
        EventStreamListener listener = eventStream.getEventStream(security.ticker());
        if (listener == null) {
            listener = eventStream.subscribe(security.board().toString(), security.ticker());
        }
        Drone drone = new GrpcFollowerDrone(security, strategy, listener);

    }

}
