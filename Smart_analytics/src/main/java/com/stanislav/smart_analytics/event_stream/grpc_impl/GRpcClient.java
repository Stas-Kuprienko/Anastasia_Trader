package com.stanislav.smart_analytics.event_stream.grpc_impl;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class GRpcClient implements AutoCloseable {

    private final ManagedChannel channel;
    private final ScheduledExecutorService scheduler;
    private final Authenticator authenticator;


    public GRpcClient(String resource, String token, int threadPoolSize) {
        channel = ManagedChannelBuilder.forTarget(resource).build();
        this.scheduler = Executors.newScheduledThreadPool(threadPoolSize);
        this.authenticator = Authenticator.XApiKeyAuthorization(token);
    }

    public ScheduledExecutorService getScheduler() {
        return scheduler;
    }

    public Authenticator getAuthenticator() {
        return authenticator;
    }

    public ManagedChannel getChannel() {
        return channel;
    }

    @Override
    public void close() {
        channel.shutdownNow();
        scheduler.shutdown();
    }
}