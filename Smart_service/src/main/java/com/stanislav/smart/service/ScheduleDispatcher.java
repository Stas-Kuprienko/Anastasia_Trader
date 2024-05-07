package com.stanislav.smart.service;

import javax.annotation.PreDestroy;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class ScheduleDispatcher implements AutoCloseable {

    private final ScheduledExecutorService scheduledExecutor;


    public ScheduleDispatcher(int threadPoolSize) {
        this.scheduledExecutor = Executors.newScheduledThreadPool(threadPoolSize);
    }

    public ScheduledExecutorService getScheduledExecutor() {
        return scheduledExecutor;
    }

    @PreDestroy
    @Override
    public void close() {
        scheduledExecutor.shutdown();
    }
}