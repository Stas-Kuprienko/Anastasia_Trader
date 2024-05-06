package com.stanislav.smart.service;

import java.util.concurrent.ScheduledExecutorService;

public interface SmartService {

    ScheduledExecutorService getScheduledExecutor();
}