package com.stanislav.event_stream.finam;

import com.stanislav.event_stream.EventStreamService;
import com.stanislav.event_stream.grpc_impl.Authenticator;
import com.stanislav.event_stream.grpc_impl.gRpcClient;
import grpc.tradeapi.v1.EventsGrpc;
import io.grpc.stub.StreamObserver;
import proto.tradeapi.v1.Events;

public class FinamEventStreamService extends gRpcClient implements EventStreamService {

    private static final String ORDER_BOOK_REQUEST_ID = "32ef5786-e887";

    private final Authenticator authenticator;


    public FinamEventStreamService(String resource, String apiToken) {
        super(resource);
        this.authenticator = Authenticator.XApiKeyAuthorization(apiToken);
    }


    @Override
    public void subscribe(String ticker, String board) throws InterruptedException {
        Events.SubscriptionRequest request = buildRequest(ticker, board);
        StreamObserver<Events.SubscriptionRequest> subscriptionRequest = buildStreamObserverRequest();

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


    private Events.SubscriptionRequest buildRequest(String ticker, String board) {
        Events.OrderBookSubscribeRequest orderBookSubscribeRequest = Events.OrderBookSubscribeRequest.newBuilder()
                .setRequestId(ORDER_BOOK_REQUEST_ID)
                .setSecurityCode(ticker)
                .setSecurityBoard(board)
                .build();

        return Events.SubscriptionRequest.newBuilder().setOrderBookSubscribeRequest(orderBookSubscribeRequest).build();
    }

    private StreamObserver<Events.SubscriptionRequest> buildStreamObserverRequest() {
        EventsGrpc.EventsStub stub = EventsGrpc.newStub(channel).withCallCredentials(authenticator);
        StreamObserver<Events.Event> response = new OrderBookObserver();
        return stub.getEvents(response);
    }
}
