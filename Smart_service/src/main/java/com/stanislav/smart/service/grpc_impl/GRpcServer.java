package com.stanislav.smart.service.grpc_impl;

import com.stanislav.smart.service.grpc_impl.security.ServerSecurityInterceptor;
import io.grpc.Server;
import io.grpc.ServerBuilder;

import javax.annotation.PreDestroy;
import java.io.IOException;

public class GRpcServer implements AutoCloseable {

    private final Server server;

    public GRpcServer(String secretKey, int port) {

        this.server = ServerBuilder.forPort(port)
                .intercept(new ServerSecurityInterceptor(secretKey))
                .addService(new SmartAutoTradeGRpcService())
                .build();
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