package com.stanislav.smart.domain.controller;

import com.stanislav.smart.domain.automation.Drone;
import com.stanislav.smart.domain.automation.TradingStrategy;
import com.stanislav.smart.domain.entities.Security;
import io.grpc.stub.StreamObserver;
import stanislav.anastasia.trade.Smart;

public interface DroneLauncher <REQUEST, RESPONSE> {

    Drone launchDrone(REQUEST request, RESPONSE response);

    //TODO
    boolean stopDrone(Security security);
}