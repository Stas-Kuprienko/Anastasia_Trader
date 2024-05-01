package com.stanislav.smart.domain.event_stream.finam;

import com.stanislav.smart.domain.event_stream.EventStreamKit;
import com.stanislav.smart.service.grpc_impl.GRpcClient;
import com.stanislav.smart.domain.event_stream.EventStream;

public class FinamGrpcEventStreamKit implements EventStreamKit {

    private final EventStream orderBookStreamService;


    public FinamGrpcEventStreamKit(GRpcClient grpcClient) {
        this.orderBookStreamService = new FinamOrderBookStream(grpcClient);
    }

    @Override
    public EventStream getOrderBookStreamService() {
        return orderBookStreamService;
    }
}
