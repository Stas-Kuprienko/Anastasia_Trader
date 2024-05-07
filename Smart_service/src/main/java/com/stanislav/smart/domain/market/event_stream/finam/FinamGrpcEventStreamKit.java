package com.stanislav.smart.domain.market.event_stream.finam;

import com.stanislav.smart.domain.market.event_stream.EventStream;
import com.stanislav.smart.domain.market.event_stream.EventStreamKit;
import com.stanislav.smart.service.grpc_impl.GRpcClient;
import com.stanislav.smart.service.ScheduleDispatcher;

public class FinamGrpcEventStreamKit implements EventStreamKit {

    private final EventStream orderBookStreamService;


    public FinamGrpcEventStreamKit(ScheduleDispatcher scheduleDispatcher, GRpcClient grpcClient) {
        this.orderBookStreamService = new FinamOrderBookStream(scheduleDispatcher.getScheduledExecutor(), grpcClient);
    }

    @Override
    public EventStream getOrderBookStreamService() {
        return orderBookStreamService;
    }
}
