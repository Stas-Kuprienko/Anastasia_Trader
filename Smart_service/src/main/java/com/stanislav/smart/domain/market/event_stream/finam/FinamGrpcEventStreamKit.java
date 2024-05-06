package com.stanislav.smart.domain.market.event_stream.finam;

import com.stanislav.smart.domain.market.event_stream.EventStreamKit;
import com.stanislav.smart.service.ThreadScheduleDispatcher;
import com.stanislav.smart.service.grpc_impl.GRpcClient;
import com.stanislav.smart.domain.market.event_stream.EventStream;

public class FinamGrpcEventStreamKit implements EventStreamKit {

    private final EventStream orderBookStreamService;


    public FinamGrpcEventStreamKit(ThreadScheduleDispatcher dispatcher, GRpcClient grpcClient) {
        this.orderBookStreamService = new FinamOrderBookStream(dispatcher, grpcClient);
    }

    @Override
    public EventStream getOrderBookStreamService() {
        return orderBookStreamService;
    }
}
