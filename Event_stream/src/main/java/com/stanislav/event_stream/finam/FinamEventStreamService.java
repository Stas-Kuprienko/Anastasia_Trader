package com.stanislav.event_stream.finam;

import com.stanislav.event_stream.EventStreamService;
import io.grpc.stub.StreamObserver;
import com.stanislav.event_stream.grpc_impl.Authenticator;
import com.stanislav.event_stream.grpc_impl.gRpcClient;
import proto.tradeapi.v1.EventsGrpc;
import proto.tradeapi.v1.EventsOuterClass;

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

        EventsGrpc.EventsStub stub = EventsGrpc.newStub(channel).withCallCredentials(authenticator);

        StreamObserver<EventsOuterClass.Event> responseObserve = new OrderBookStreamObserver();

        StreamObserver<EventsOuterClass.SubscriptionRequest> requestStream = stub.getEvents(responseObserve);

        EventsOuterClass.SubscriptionRequest subscriptionRequest =
                EventsOuterClass.SubscriptionRequest.newBuilder().setOrderBookSubscribeRequest(request).build();

        for (int i = 0; i < 10; i++) {
            try {
                requestStream.onNext(subscriptionRequest);

                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        requestStream.onCompleted();

        //TODO !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    }

    @Override
    public void unsubscribe(String board, String ticker) {

    }


    public static class OrderBookStreamObserver implements StreamObserver<EventsOuterClass.Event> {

        @Override
        public void onNext(EventsOuterClass.Event event) {
            if (event.hasOrderBook()) {
                //TODO
                System.out.println(event.getOrderBook());
            }
        }

        @Override
        public void onError(Throwable throwable) {
            //TODO
            throwable.printStackTrace();
        }

        @Override
        public void onCompleted() {
            //TODO
            System.out.println("on complete");
        }
    }
}
