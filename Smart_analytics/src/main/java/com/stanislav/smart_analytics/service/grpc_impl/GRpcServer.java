package com.stanislav.smart_analytics.service.grpc_impl;

import io.grpc.Server;
import io.grpc.ServerBuilder;

import javax.annotation.PreDestroy;
import java.io.IOException;

public class GRpcServer implements AutoCloseable {

    private final Server server;
    private final String token;

    public GRpcServer(String token, int port) {
        this.server = ServerBuilder.forPort(port)
                .addService(new SmartAutoTradeGRpcService())
                .build();
        this.token = token;
        try {
            server.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    @PreDestroy
    public void close() {
        server.shutdownNow();
    }
}