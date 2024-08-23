package com.anastasia.smart.domain.utils;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class StreamObserverDriver <LISTENER> {

    private final ScheduledExecutorService scheduledExecutorService;
    private final Runnable runnable;
    private final Set<LISTENER> listeners;
    private ScheduledFuture<?> scheduledFuture;


    public StreamObserverDriver(ScheduledExecutorService scheduledExecutorService, Runnable runnable) {
        this.scheduledExecutorService = scheduledExecutorService;
        this.runnable = runnable;
        listeners = new HashSet<>();
    }


    public void start() {
        if (scheduledFuture == null || scheduledFuture.isDone()) {
            scheduledFuture = scheduledExecutorService.schedule(runnable, 1, TimeUnit.SECONDS);
        }
    }

    public void stop(boolean force) {
        if (scheduledFuture != null && !scheduledFuture.isDone()) {
            if (force || listeners.isEmpty()) {
                scheduledFuture.cancel(true);
            }
        }
    }

    public void addListener(LISTENER listener) {
        listeners.add(listener);
    }

    public void removeListener(LISTENER listener) {
        listeners.remove(listener);
        if (listeners.isEmpty()) {
            stop(false);
        }
    }

    public boolean isDisabled() {
        return scheduledFuture.isDone();
    }
}
