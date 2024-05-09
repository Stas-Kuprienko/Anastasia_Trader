package com.stanislav.smart.domain.automation;

import com.stanislav.smart.domain.entities.Security;

import java.util.concurrent.ScheduledFuture;

public interface Drone extends Runnable {

    Security getSecurity();

    TradingStrategy getStrategy();

    void launch();

    ScheduledFuture<?> getScheduledFuture();

    void setScheduledFuture(ScheduledFuture<?> scheduledFuture);

    boolean stop();
}
