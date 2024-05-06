package com.stanislav.smart.service;

import com.stanislav.smart.domain.automation.drones.Drone;

import javax.annotation.PreDestroy;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class ThreadScheduleDispatcher {

    private final ScheduledExecutorService scheduledExecutor;

    private final ConcurrentHashMap<String, Drone> drones;

    public ThreadScheduleDispatcher(int threadPoolSize) {
        this.scheduledExecutor = Executors.newScheduledThreadPool(threadPoolSize);
        this.drones = new ConcurrentHashMap<>();
    }




    public ScheduledExecutorService getScheduledExecutor() {
        return scheduledExecutor;
    }

    @PreDestroy
    public void shutdown() {
        scheduledExecutor.shutdown();
    }
}
