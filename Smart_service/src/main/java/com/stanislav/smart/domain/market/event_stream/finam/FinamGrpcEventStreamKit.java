package com.stanislav.smart.domain.market.event_stream.finam;

import com.stanislav.smart.domain.market.event_stream.EventStream;
import com.stanislav.smart.domain.market.event_stream.EventStreamKit;
import com.stanislav.smart.service.SmartService;
import com.stanislav.smart.service.grpc_impl.GRpcClient;

public class FinamGrpcEventStreamKit implements EventStreamKit {

    private final EventStream orderBookStreamService;


    public FinamGrpcEventStreamKit(SmartService smartService, GRpcClient grpcClient) {
        this.orderBookStreamService = new FinamOrderBookStream(smartService.getScheduledExecutor(), grpcClient);
    }

    @Override
    public EventStream getOrderBookStreamService() {
        return orderBookStreamService;
    }
}
