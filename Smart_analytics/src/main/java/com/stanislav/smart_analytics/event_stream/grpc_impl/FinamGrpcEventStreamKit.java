package com.stanislav.smart_analytics.event_stream.grpc_impl;

import com.stanislav.smart_analytics.event_stream.EventStreamKit;
import com.stanislav.smart_analytics.event_stream.finam.FinamOrderBookStream;
import com.stanislav.smart_analytics.event_stream.service.EventStream;

import javax.annotation.PreDestroy;

public class FinamGrpcEventStreamKit implements EventStreamKit {

    private final GRpcClient grpcClient;

    private final EventStream orderBookStreamService;


    public FinamGrpcEventStreamKit(String resource, String token, int threadPoolSize) {
        this.grpcClient = new GRpcClient(resource, token, threadPoolSize);
        this.orderBookStreamService = new FinamOrderBookStream(grpcClient);
    }

    @Override
    public EventStream getOrderBookStreamService() {
        return orderBookStreamService;
    }

    @PreDestroy
    public void destroy() {
        grpcClient.close();
    }
}
