package com.stanislav.smart_analytics.service.grpc_impl;

import com.stanislav.smart_analytics.service.SmartAutoTradeAPI;
import io.grpc.Server;
import io.grpc.ServerBuilder;

import javax.annotation.PreDestroy;
import java.io.IOException;

public class GRpcAPI implements SmartAutoTradeAPI {

    private final Server server;
    private final String token;

    public GRpcAPI(String token, int port) {
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



    @PreDestroy
    public void destroy() {
        server.shutdownNow();
    }
}