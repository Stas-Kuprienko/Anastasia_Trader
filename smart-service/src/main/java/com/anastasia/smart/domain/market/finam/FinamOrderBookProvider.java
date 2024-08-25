package com.anastasia.smart.domain.market.finam;

import com.anastasia.smart.configuration.grpc_impl.GRpcClient;
import com.anastasia.smart.domain.automation.TradeStrategy;
import com.anastasia.smart.domain.market.OrderBookProvider;
import com.anastasia.smart.domain.market.order_book.*;
import com.anastasia.smart.domain.market.order_book.finam.FinamOrderBookEntry;
import com.anastasia.smart.domain.market.order_book.finam.FinamOrderBookStream;
import com.anastasia.smart.domain.market.order_book.finam.FinamOrderBookStreamObserver;
import com.anastasia.smart.domain.market.order_book.finam.OrderBookStreamWrapper;
import com.anastasia.smart.domain.utils.StreamObserverDriver;
import com.anastasia.trade.Smart;
import grpc.tradeapi.v1.EventsGrpc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import proto.tradeapi.v1.Events;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;

@Component
public class FinamOrderBookProvider implements OrderBookProvider<Events.OrderBookRow> {

    private static final String ORDER_BOOK_REQUEST_ID = "32ef5786-e887";

    private final ScheduledExecutorService scheduledExecutorService;
    private final EventsGrpc.EventsStub stub;
    private final ConcurrentHashMap<Smart.Security, OrderBookStreamWrapper> streamStore;


    @Autowired
    public FinamOrderBookProvider(ScheduledExecutorService scheduledExecutorService, GRpcClient rpcClient) {
        this.scheduledExecutorService = scheduledExecutorService;
        this.stub = EventsGrpc.newStub(rpcClient.getChannel()).withCallCredentials(rpcClient.getAuthenticator());
        streamStore = new ConcurrentHashMap<>();
    }


    @Override
    public OrderBookStream<Events.OrderBookRow> subscribe(Smart.Security security, TradeStrategy strategy) {
        var streamWrapper = streamStore.get(security);
        if (streamWrapper == null) {
            var subscription = buildSubscribeRequest(security);
            var unsubscription = buildUnsubscribeRequest(security);
            var orderBookStream = enable(subscription);
            orderBookStream.addListener(strategy);
            streamWrapper = new OrderBookStreamWrapper(orderBookStream, subscription, unsubscription);
            streamStore.put(security, streamWrapper);
        }
        return streamWrapper.orderBookStream();
    }

    @Override
    public void unsubscribe(Smart.Security security) {
        var wrapper = streamStore.get(security);
        if (wrapper != null) {
            FinamOrderBookStreamObserver streamObserver =
                    (FinamOrderBookStreamObserver) wrapper.orderBookStream().getStreamObserver();
            var observer = stub.getEvents(streamObserver);
            observer.onCompleted();
            observer.onNext(wrapper.unsubscription());
            observer.onCompleted();
        }
    }


    private OrderBookStream<Events.OrderBookRow> enable(Events.SubscriptionRequest subscription) {
        FinamOrderBookStreamObserver streamObserver = new FinamOrderBookStreamObserver(new FinamOrderBookEntry());
        var streamObserverDriver = new StreamObserverDriver<TradeStrategy>(
                scheduledExecutorService,
                () -> stub.getEvents(streamObserver).onNext(subscription));
        return new FinamOrderBookStream(streamObserver, streamObserverDriver);
    }


    private Events.SubscriptionRequest buildSubscribeRequest(Smart.Security security) {
        var orderBookSubscribeRequest = Events.OrderBookSubscribeRequest.newBuilder()
                .setRequestId(ORDER_BOOK_REQUEST_ID)
                .setSecurityCode(security.getTicker())
                .setSecurityBoard(security.getBoard())
                .build();

        return Events.SubscriptionRequest.newBuilder()
                .setOrderBookSubscribeRequest(orderBookSubscribeRequest).build();
    }

    private Events.SubscriptionRequest buildUnsubscribeRequest(Smart.Security security) {
        var orderBookUnsubscribeRequest = Events.OrderBookUnsubscribeRequest.newBuilder()
                .setRequestId(ORDER_BOOK_REQUEST_ID)
                .setSecurityCode(security.getTicker())
                .setSecurityBoard(security.getBoard())
                .build();

        return Events.SubscriptionRequest.newBuilder()
                .setOrderBookUnsubscribeRequest(orderBookUnsubscribeRequest).build();
    }
}
