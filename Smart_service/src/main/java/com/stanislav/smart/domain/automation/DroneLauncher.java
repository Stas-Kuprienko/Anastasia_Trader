package com.stanislav.smart.domain.automation;

import com.stanislav.smart.domain.automation.Drone;
import io.grpc.stub.StreamObserver;
import stanislav.anastasia.trade.Smart;

public interface DroneLauncher {

    Drone launchDrone(Smart.SubscribeTradeRequest request, StreamObserver<Smart.SubscribeTradeResponse> response);

    boolean stopDrone(Smart.Security security);
}