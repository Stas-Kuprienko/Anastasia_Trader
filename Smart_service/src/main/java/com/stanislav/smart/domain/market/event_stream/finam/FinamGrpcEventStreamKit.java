package com.stanislav.smart.domain.market.event_stream.finam;

import com.stanislav.smart.domain.market.event_stream.EventStream;
import com.stanislav.smart.domain.market.event_stream.EventStreamKit;
import com.stanislav.smart.service.grpc_impl.GRpcClient;

import java.util.concurrent.ScheduledExecutorService;

public class FinamGrpcEventStreamKit implements EventStreamKit {

    private final EventStream orderBookStreamService;


    public FinamGrpcEventStreamKit(ScheduledExecutorService scheduledExecutorService, GRpcClient grpcClient) {
        this.orderBookStreamService = new FinamOrderBookStream(scheduledExecutorService, grpcClient);
    }

    @Override
    public EventStream getOrderBookStreamService() {
        return orderBookStreamService;
    }
}
