package com.stanislav.domain.smart.service;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import javax.annotation.PreDestroy;

public class GRpcConnection implements AutoCloseable {

    private final ManagedChannel channel;

    public GRpcConnection(String resource) {
        this.channel = ManagedChannelBuilder.forTarget(resource).usePlaintext().build();
    }


    public ManagedChannel getChannel() {
        return channel;
    }

    @Override
    @PreDestroy
    public void close() {
        channel.shutdownNow();
    }
}