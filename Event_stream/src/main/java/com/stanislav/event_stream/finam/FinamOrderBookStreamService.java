package com.stanislav.event_stream.finam;

import com.stanislav.event_stream.OrderBookStreamService;
import com.stanislav.event_stream.grpc_impl.Authenticator;
import com.stanislav.event_stream.grpc_impl.gRpcClient;
import grpc.tradeapi.v1.EventsGrpc;
import io.grpc.stub.StreamObserver;
import proto.tradeapi.v1.Events;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;

public class FinamOrderBookStreamService extends gRpcClient implements OrderBookStreamService {

    private static final String ORDER_BOOK_REQUEST_ID = "32ef5786-e887";
    private final Authenticator authenticator;

    private final ScheduledExecutorService scheduler;
    private final ConcurrentHashMap<String, Future<Collector>> eventStreams;


    public FinamOrderBookStreamService(String resource, String apiToken, int threadPoolSize) {
        super(resource);
        this.authenticator = Authenticator.XApiKeyAuthorization(apiToken);
        this.scheduler = Executors.newScheduledThreadPool(threadPoolSize);
        this.eventStreams = new ConcurrentHashMap<>();
    }


    @Override
    public void subscribe(String ticker, String board) throws InterruptedException {
        EventsGrpc.EventsStub stub = EventsGrpc.newStub(channel).withCallCredentials(authenticator);
        var request = buildSubscriptionRequest(ticker, board);

        eventStreams.put(ticker, scheduler.submit(new OrderBookStreamListener(request, stub)));
        //TODO need to fix !!!!!!!!
    }

    @Override
    public void unsubscribe(String board, String ticker) {

    }

    public ConcurrentHashMap<String, Future<Collector>> getEventStreams() {
        return eventStreams;
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