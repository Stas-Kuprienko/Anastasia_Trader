package com.stanislav.event_stream.finam;

import com.stanislav.event_stream.OrderBookRow;
import com.stanislav.event_stream.OrderBookStreamService;
import com.stanislav.event_stream.grpc_impl.Authenticator;
import com.stanislav.event_stream.grpc_impl.gRpcClient;
import grpc.tradeapi.v1.EventsGrpc;
import io.grpc.stub.StreamObserver;
import proto.tradeapi.v1.Events;

public class FinamOrderBookStreamService extends gRpcClient implements OrderBookStreamService {

    private static final String ORDER_BOOK_REQUEST_ID = "32ef5786-e887";

    private final Authenticator authenticator;
    private final Collector collector;
    private final OrderBookStreamListener listener;


    public FinamOrderBookStreamService(String resource, String apiToken) {
        super(resource);
        this.authenticator = Authenticator.XApiKeyAuthorization(apiToken);
        this.collector = new Collector(20);
        this.listener = new OrderBookStreamListener(collector);
    }


    @Override
    public void subscribe(String ticker, String board) throws InterruptedException {
        var request = buildSubscriptionRequest(ticker, board);
        var subscriptionRequest = buildStreamObserverSubscriptionRequest();

        //TODO this is a temporary solution, need to fix !!!!!!!!
        for (int i = 0; i < 10; i++) {
            Thread.sleep(1000);
            subscriptionRequest.onNext(request);
        }
        subscriptionRequest.onCompleted();
        close();
    }

    @Override
    public void unsubscribe(String board, String ticker) {

    }

    @Override
    public OrderBookRow currentAsk() {
        return new FinamOrderBookRowProxy(collector.currentAsk());
    }

    @Override
    public OrderBookRow currentBid() {
        return new FinamOrderBookRowProxy(collector.currentBid());
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

    private StreamObserver<Events.SubscriptionRequest> buildStreamObserverSubscriptionRequest() {
        EventsGrpc.EventsStub stub = EventsGrpc.newStub(channel).withCallCredentials(authenticator);
        return stub.getEvents(new OrderBookObserver(listener));
    }
}
