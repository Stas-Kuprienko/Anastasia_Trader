package com.stanislav.smart.domain.automation;

import stanislav.anastasia.trade.Smart;

import java.util.concurrent.ScheduledFuture;

public interface Drone extends Runnable {

    Smart.Security getSecurity();

    TradingStrategy getStrategy();

    void launch();

    ScheduledFuture<?> getScheduledFuture();

    void setScheduledFuture(ScheduledFuture<?> scheduledFuture);

    boolean stop();
}
