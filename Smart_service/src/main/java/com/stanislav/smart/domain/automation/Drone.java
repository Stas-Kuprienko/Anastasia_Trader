package com.stanislav.smart.domain.automation;

import stanislav.anastasia.trade.Smart;

import java.util.concurrent.ScheduledFuture;

public interface Drone extends Runnable {

    void addAccount(Smart.Account account);

    void removeAccount(Smart.Account account);

    void launch();

    ScheduledFuture<?> getScheduledFuture();

    void setScheduledFuture(ScheduledFuture<?> scheduledFuture);

    boolean isUseless();

    boolean stop();
}
