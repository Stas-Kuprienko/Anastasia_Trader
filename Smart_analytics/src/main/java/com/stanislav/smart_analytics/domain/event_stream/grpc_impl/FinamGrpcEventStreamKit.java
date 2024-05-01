package com.stanislav.smart_analytics.domain.event_stream.grpc_impl;

import com.stanislav.smart_analytics.domain.event_stream.EventStreamKit;
import com.stanislav.smart_analytics.domain.event_stream.finam.FinamOrderBookStream;
import com.stanislav.smart_analytics.domain.event_stream.service.EventStream;

import javax.annotation.PreDestroy;

public class FinamGrpcEventStreamKit implements EventStreamKit {

    private final EventStream orderBookStreamService;


    public FinamGrpcEventStreamKit(GRpcClient gRpcClient) {
        this.orderBookStreamService = new FinamOrderBookStream(gRpcClient);
    }

    @Override
    public EventStream getOrderBookStreamService() {
        return orderBookStreamService;
    }
}
