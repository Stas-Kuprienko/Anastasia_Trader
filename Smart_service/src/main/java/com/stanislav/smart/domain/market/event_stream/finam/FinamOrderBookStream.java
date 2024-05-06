package com.stanislav.smart.domain.market.event_stream.finam;

import com.stanislav.smart.domain.market.event_stream.EventStreamListener;
import com.stanislav.smart.domain.market.event_stream.EventStream;
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
    private final ConcurrentHashMap<String, OrderBookStreamListener> eventStreamMap;


    public FinamOrderBookStream(GRpcClient rpcClient) {
        this.scheduler = rpcClient.getScheduler();
        this.stub = EventsGrpc.newStub(rpcClient.getChannel()).withCallCredentials(rpcClient.getAuthenticator());
        this.eventStreamMap = new ConcurrentHashMap<>();
    }


    @Override
    public EventStreamListener subscribe(String ticker, String board) {
        var subscribe = buildSubscribeRequest(ticker, board);
        var unsubscribe = buildUnsubscribeRequest(ticker, board);
        OrderBookStreamListener listener = new OrderBookStreamListener(subscribe, unsubscribe, stub);
        var eventStream =
                scheduler.scheduleAtFixedRate(listener.initStreamThread(), 1, 1, TimeUnit.SECONDS);

        listener.setScheduledFuture(eventStream);
        eventStreamMap.put(ticker, listener);
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
    public OrderBookStreamListener getEventStream(String ticker) {
        return eventStreamMap.get(ticker);
    }


    private Events.SubscriptionRequest buildSubscribeRequest(String ticker, String board) {
        var orderBookSubscribeRequest = Events.OrderBookSubscribeRequest.newBuilder()
                .setRequestId(ORDER_BOOK_REQUEST_ID)
                .setSecurityCode(ticker)
                .setSecurityBoard(board)
                .build();

        return Events.SubscriptionRequest.newBuilder()
                .setOrderBookSubscribeRequest(orderBookSubscribeRequest).build();
    }

    private Events.SubscriptionRequest buildUnsubscribeRequest(String ticker, String board) {
        var orderBookUnsubscribeRequest = Events.OrderBookUnsubscribeRequest.newBuilder()
                .setRequestId(ORDER_BOOK_REQUEST_ID)
                .setSecurityCode(ticker)
                .setSecurityBoard(board)
                .build();

        return Events.SubscriptionRequest.newBuilder()
                .setOrderBookUnsubscribeRequest(orderBookUnsubscribeRequest).build();
    }
}