package com.stanislav.smart.service.grpc_impl;

import com.stanislav.smart.domain.automation.grpc_impl.StrategiesGrpcDispatcher;
import com.stanislav.smart.service.SmartService;
import com.stanislav.smart.service.grpc_impl.security.ServerSecurityInterceptor;
import io.grpc.Server;
import io.grpc.ServerBuilder;

import javax.annotation.PreDestroy;
import java.io.IOException;

public class GRpcServer implements AutoCloseable {

    StrategiesGrpcDispatcher strategiesDispatcher;
    private final Server server;

    public GRpcServer(String appId, String secretKey, int port, SmartService smartService) {
        this.server = ServerBuilder.forPort(port)
                .intercept(new ServerSecurityInterceptor(appId, secretKey))
                .addService(new SmartAutoTradeGRpcImpl(smartService, strategiesDispatcher))
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