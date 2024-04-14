package com.stanislav.event_stream.finam;

import com.stanislav.event_stream.EventStreamService;
import com.stanislav.event_stream.grpc_impl.Authenticator;
import com.stanislav.event_stream.grpc_impl.gRpcClient;
import io.grpc.stub.StreamObserver;
import proto.tradeapi.v1.EventsGrpc;
import proto.tradeapi.v1.EventsOuterClass;

import java.util.concurrent.CountDownLatch;

public class FinamEventStreamService extends gRpcClient implements EventStreamService {

    private static final String ORDER_BOOK_REQUEST_ID = "32ef5786-e887";

    private final Authenticator authenticator;


    public FinamEventStreamService(String resource, String apiToken) {
        super(resource);
        this.authenticator = Authenticator.XApiKeyAuthorization(apiToken);
    }


    @Override
    public void subscribe(String ticker, String board) {
        EventsOuterClass.OrderBookSubscribeRequest request = EventsOuterClass.OrderBookSubscribeRequest.newBuilder()
                .setRequestId(ORDER_BOOK_REQUEST_ID)
                .setSecurityBoard(board)
                .setSecurityCode(ticker).build();

        CountDownLatch countDown = new CountDownLatch(1);

        EventsGrpc.EventsStub stub = EventsGrpc.newStub(channel).withCallCredentials(authenticator);

        StreamObserver<EventsOuterClass.Event> responseObserve = new StreamObserver<>() {

            @Override
            public void onNext(EventsOuterClass.Event event) {
                System.out.println(event.toString());
                System.out.println("notify");
            }

            @Override
            public void onError(Throwable throwable) {
                countDown.countDown();
            }

            @Override
            public void onCompleted() {
                countDown.countDown();
            }
        };

        StreamObserver<EventsOuterClass.SubscriptionRequest> requestStream = stub.getEvents(responseObserve);

        EventsOuterClass.SubscriptionRequest subscriptionRequest =
                EventsOuterClass.SubscriptionRequest.newBuilder().setOrderBookSubscribeRequest(request).build();

        requestStream.onNext(subscriptionRequest);
        //TODO !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    }

    @Override
    public void unsubscribe(String board, String ticker) {

    }
}
