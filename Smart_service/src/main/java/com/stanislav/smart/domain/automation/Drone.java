package com.stanislav.smart.domain.automation;

import com.stanislav.smart.domain.entities.Security;

public interface Drone extends Runnable {

    Security getSecurity();

    TradingStrategy getStrategy();

    void launch();

    void stop();
}
