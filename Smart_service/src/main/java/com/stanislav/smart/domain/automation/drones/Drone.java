package com.stanislav.smart.domain.automation.drones;

public interface Drone extends Runnable {

    void launch();

    void following();

    void stop();
}
