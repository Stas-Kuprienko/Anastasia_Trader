package com.stanislav.event_stream.grpc_impl;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public abstract class gRpcClient implements AutoCloseable {

    protected final ManagedChannel channel;


    protected gRpcClient(String host) {
        channel = ManagedChannelBuilder.forTarget(host).build();
    }


    public ManagedChannel getChannel() {
        return channel;
    }

    @Override
    public void close() {
        channel.shutdownNow();
    }
}