package com.stanislav.smart.service.grpc_impl;

import io.grpc.BindableService;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.ServerInterceptor;
import javax.annotation.PreDestroy;
import java.io.IOException;
import java.util.List;

public class GRpcFrame implements AutoCloseable {

    private final Server server;

    public GRpcFrame(int port, List<ServerInterceptor> interceptors, List<BindableService> services) {

        ServerBuilder<?> serverBuilder = ServerBuilder.forPort(port);

        if (!interceptors.isEmpty()) {
            for (ServerInterceptor interceptor : interceptors) {
                serverBuilder.intercept(interceptor);
            }
        }
        if (!services.isEmpty()) {
            for (BindableService service : services) {
                serverBuilder.addService(service);
            }
        }
        this.server = serverBuilder.build();

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