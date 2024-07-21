package com.stanislav.smart.configuration.grpc_impl;

import com.stanislav.smart.configuration.grpc_impl.security.AuthCallCredential;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import javax.annotation.PreDestroy;

public class GRpcClient implements AutoCloseable {

    private final ManagedChannel channel;
    private final AuthCallCredential authCallCredential;


    public GRpcClient(String resource, String token) {
        channel = ManagedChannelBuilder.forTarget(resource).build();
        this.authCallCredential = AuthCallCredential.XApiKeyAuthorization(token);
    }

    public AuthCallCredential getAuthenticator() {
        return authCallCredential;
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