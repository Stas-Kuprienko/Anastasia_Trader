package com.stanislav.smart.service.grpc_impl;

import com.stanislav.smart.service.grpc_impl.security.Authenticator;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import javax.annotation.PreDestroy;

public class GRpcClient implements AutoCloseable {

    private final ManagedChannel channel;
    private final Authenticator authenticator;


    public GRpcClient(String resource, String token) {
        channel = ManagedChannelBuilder.forTarget(resource).build();
        this.authenticator = Authenticator.XApiKeyAuthorization(token);
    }


    public Authenticator getAuthenticator() {
        return authenticator;
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