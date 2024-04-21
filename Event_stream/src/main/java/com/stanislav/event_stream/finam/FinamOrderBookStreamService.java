package com.stanislav.event_stream.finam;

import com.stanislav.event_stream.OrderBookStreamService;
import com.stanislav.event_stream.grpc_impl.Authenticator;
import com.stanislav.event_stream.grpc_impl.gRpcClient;
import grpc.tradeapi.v1.EventsGrpc;
import proto.tradeapi.v1.Events;

import javax.annotation.PreDestroy;
import java.util.concurrent.*;

public class FinamOrderBookStreamService extends gRpcClient implements OrderBookStreamService {

    private static final String ORDER_BOOK_REQUEST_ID = "32ef5786-e887";
    private final Authenticator authenticator;

    private final ScheduledExecutorService scheduler;
    private final ConcurrentHashMap<String, OrderBookStreamListener> eventStreamMap;


    public FinamOrderBookStreamService(String resource, String apiToken, int threadPoolSize) {
        super(resource);
        this.authenticator = Authenticator.XApiKeyAuthorization(apiToken);
        this.scheduler = Executors.newScheduledThreadPool(threadPoolSize);
        this.eventStreamMap = new ConcurrentHashMap<>();
    }


    @Override
    public void subscribe(String ticker, String board) throws InterruptedException {
        EventsGrpc.EventsStub stub = EventsGrpc.newStub(channel).withCallCredentials(authenticator);
        var request = buildSubscriptionRequest(ticker, board);

        //TODO need to fix !!!!!!!!
        OrderBookStreamListener listener = new OrderBookStreamListener(request,stub);
        var eventStream =
                scheduler.scheduleAtFixedRate(listener.initStreamObserveThread(), 1, 1, TimeUnit.SECONDS);
        listener.setScheduledFuture(eventStream);
        eventStreamMap.put(ticker, listener);
    }

    @Override
    public void unsubscribe(String board, String ticker) {

    }

    public OrderBookStreamListener getEventStream(String ticker) {
        return eventStreamMap.get(ticker);
    }

    @PreDestroy
    public void shutdown() {
        scheduler.shutdown();
    }

    private Events.SubscriptionRequest buildSubscriptionRequest(String ticker, String board) {
        var orderBookSubscribeRequest = Events.OrderBookSubscribeRequest.newBuilder()
                .setRequestId(ORDER_BOOK_REQUEST_ID)
                .setSecurityCode(ticker)
                .setSecurityBoard(board)
                .build();

        return Events.SubscriptionRequest.newBuilder()
                .setOrderBookSubscribeRequest(orderBookSubscribeRequest).build();
    }
}