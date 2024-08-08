/*
 * Stanislav Kuprienko *** Omsk, Russia
 */

package com.anastasia.trade.domain.smart.finam_grpc;

import com.google.protobuf.Empty;
import com.anastasia.trade.domain.market.grpc.Authentication;
import com.anastasia.trade.domain.market.grpc.GRpcConnection;
import com.anastasia.trade.domain.notification.SmartSubscribeNotificationService;
import com.anastasia.trade.domain.smart.SmartAutoTradeService;
import com.anastasia.trade.entities.Board;
import com.anastasia.trade.entities.Broker;
import com.anastasia.trade.entities.TimeFrame;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import stanislav.anastasia.trade.Smart;
import stanislav.anastasia.trade.SmartAutoTradeGrpc;
import java.util.HashSet;
import java.util.Set;

@Slf4j
public class SmartAutoTradeImpl implements SmartAutoTradeService {

    private final SmartSubscribeNotificationService notificationService;
    private final SmartAutoTradeGrpc.SmartAutoTradeStub stub;
    private final Set<String> strategies;


    public SmartAutoTradeImpl(SmartSubscribeNotificationService notificationService, String token, GRpcConnection connection) {
        this.notificationService = notificationService;
        this.stub = SmartAutoTradeGrpc.newStub(connection.getChannel()).
                withCallCredentials(Authentication.xApiKeyAuthorization(token));
        strategies = new HashSet<>();
    }


    @Override
    public Set<String> getStrategies() {
        stub.getStrategies(Empty.newBuilder().build(), new StrategiesListResponse());
        return strategies;
    }

    @Override
    public void subscribe(String clientId, Broker broker, String ticker, Board board, String strategyName, TimeFrame.Scope timeFrame, String token) {

        Smart.Account account = Smart.Account.newBuilder()
                .setBroker(broker.name())
                .setClientId(clientId)
                .setToken(token)
                .build();
        Smart.Security security = Smart.Security.newBuilder()
                .setTicker(ticker)
                .setBoard(board.toString())
                .build();
        Smart.Strategy strategy = Smart.Strategy.newBuilder()
                .setName(strategyName)
                .setTimeFrame(TimeFrame.parse(timeFrame))
                .build();
        Smart.SubscribeTradeRequest request = Smart.SubscribeTradeRequest.newBuilder()
                .setAccount(account)
                .setSecurity(security)
                .setStrategy(strategy)
                .build();

        SubscribeTradeResponse response = new SubscribeTradeResponse(notificationService);
        stub.subscribe(request, response);
    }

    @Override
    public void unsubscribe(String clientId, Broker broker, String ticker, Board board, String strategyName, TimeFrame.Scope timeFrame, String token) {
        Smart.Account account = Smart.Account.newBuilder()
                .setBroker(broker.name())
                .setClientId(clientId)
                .setToken(token)
                .build();
        Smart.Security security = Smart.Security.newBuilder()
                .setTicker(ticker)
                .setBoard(board.toString())
                .build();
        Smart.Strategy strategy = Smart.Strategy.newBuilder()
                .setName(strategyName)
                .setTimeFrame(TimeFrame.parse(timeFrame))
                .build();

        StreamObserver<Smart.UnsubscribeResponse> responseStreamObserver = new UnsubscribeResponseObserver();

        Smart.UnsubscribeRequest request = Smart.UnsubscribeRequest.newBuilder()
                .setAccount(account)
                .setSecurity(security)
                .setStrategy(strategy)
                .build();

        stub.unsubscribe(request, responseStreamObserver);
    }


    public class StrategiesListResponse implements StreamObserver<Smart.StrategiesList> {

        @Override
        public void onNext(Smart.StrategiesList value) {
            if (!value.getItemList().isEmpty()) {
                strategies.clear();
                strategies.addAll(new HashSet<>(value.getItemList()));
            }
        }

        @Override
        public void onError(Throwable t) {
            log.error(t.getMessage());
        }

        @Override
        public void onCompleted() {
            log.info("Smart service strategies is up-to-date\n" + strategies);
        }
    }
}