package com.stanislav.smart.domain.market.event_stream.finam;

import com.stanislav.smart.domain.entities.Security;
import com.stanislav.smart.domain.market.event_stream.EventStream;
import com.stanislav.smart.domain.market.event_stream.EventStreamListener;
import com.stanislav.smart.service.grpc_impl.GRpcClient;
import grpc.tradeapi.v1.EventsGrpc;
import proto.tradeapi.v1.Events;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class FinamOrderBookStream implements EventStream {

    private static final String ORDER_BOOK_REQUEST_ID = "32ef5786-e887";

    private final ScheduledExecutorService scheduler;
    private final EventsGrpc.EventsStub stub;
    private final ConcurrentHashMap<Security, OrderBookStreamListener> eventStreamMap;


    public FinamOrderBookStream(ScheduledExecutorService scheduler, GRpcClient rpcClient) {
        this.scheduler = scheduler;
        this.stub = EventsGrpc.newStub(rpcClient.getChannel()).withCallCredentials(rpcClient.getAuthenticator());
        this.eventStreamMap = new ConcurrentHashMap<>();
    }


    @Override
    public EventStreamListener subscribe(Security security) {
        var subscribe = buildSubscribeRequest(security);
        var unsubscribe = buildUnsubscribeRequest(security);
        OrderBookStreamListener listener = new OrderBookStreamListener(subscribe, unsubscribe, stub);
        var eventStream =
                scheduler.scheduleAtFixedRate(listener.initStreamThread(), 1, 1, TimeUnit.SECONDS);

        listener.setScheduledFuture(eventStream);
        eventStreamMap.put(security, listener);
        return listener;
    }

    @Override
    public void unsubscribe(EventStreamListener listener) {
        unsubscribe(listener, false);
    }

    @Override
    public void unsubscribe(EventStreamListener listener, boolean isForced) {
        if (isForced || !(listener.isUsing())) {
            listener.getScheduledFuture().cancel(true);
            listener.stopStream();
        }
    }

    @Override
    public OrderBookStreamListener getEventStream(Security security) {
        return eventStreamMap.get(security);
    }


    private Events.SubscriptionRequest buildSubscribeRequest(Security security) {
        var orderBookSubscribeRequest = Events.OrderBookSubscribeRequest.newBuilder()
                .setRequestId(ORDER_BOOK_REQUEST_ID)
                .setSecurityCode(security.ticker())
                .setSecurityBoard(security.board().toString())
                .build();

        return Events.SubscriptionRequest.newBuilder()
                .setOrderBookSubscribeRequest(orderBookSubscribeRequest).build();
    }

    private Events.SubscriptionRequest buildUnsubscribeRequest(Security security) {
        var orderBookUnsubscribeRequest = Events.OrderBookUnsubscribeRequest.newBuilder()
                .setRequestId(ORDER_BOOK_REQUEST_ID)
                .setSecurityCode(security.ticker())
                .setSecurityBoard(security.board().toString())
                .build();

        return Events.SubscriptionRequest.newBuilder()
                .setOrderBookUnsubscribeRequest(orderBookUnsubscribeRequest).build();
    }
}