package com.stanislav.event_stream.grpc_impl;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import javax.annotation.PreDestroy;

public abstract class gRpcClient implements AutoCloseable {

    protected final ManagedChannel channel;


    protected gRpcClient(String host) {
        channel = ManagedChannelBuilder.forTarget(host).build();
    }


    public ManagedChannel getChannel() {
        return channel;
    }

    @PreDestroy
    @Override
    public void close() {
        channel.shutdownNow();
    }
}