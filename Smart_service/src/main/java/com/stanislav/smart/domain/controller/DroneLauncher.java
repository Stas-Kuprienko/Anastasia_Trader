package com.stanislav.smart.domain.controller;

import com.stanislav.smart.domain.automation.Drone;

public interface DroneLauncher <REQUEST, RESPONSE> {

    Drone launchDrone(REQUEST request, RESPONSE responseObserver);
}