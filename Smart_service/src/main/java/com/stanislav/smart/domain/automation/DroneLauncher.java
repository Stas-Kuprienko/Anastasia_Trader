package com.stanislav.smart.domain.automation;

import io.grpc.stub.StreamObserver;
import stanislav.anastasia.trade.Smart;

public interface DroneLauncher {

    void launchDrone(Smart.SubscribeTradeRequest request, StreamObserver<Smart.SubscribeTradeResponse> response);

    void removeAccount(Smart.UnsubscribeRequest unsubscribeRequest);

    boolean stopDrone(Smart.Security security, Smart.Strategy strategy);
}