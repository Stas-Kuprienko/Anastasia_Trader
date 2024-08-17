package com.anastasia.smart.domain.automation.grpc_impl;

import com.anastasia.smart.domain.automation.Drone;
import com.anastasia.smart.domain.automation.TradeStrategy;
import com.anastasia.smart.entities.Board;
import com.anastasia.smart.entities.Direction;
import com.anastasia.smart.entities.TimeFrame;
import com.anastasia.smart.entities.criteria.NewOrderCriteria;
import com.anastasia.smart.domain.trade.TradeDealingManager;
import io.grpc.stub.StreamObserver;
import com.anastasia.trade.Smart;
import java.time.Duration;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Future;

public class GrpcFollowerDrone implements Drone {

    private final TradeDealingManager dealingManager;
    private final TradeStrategy strategy;
    private final Smart.Security security;
    private final StreamObserver<Smart.SubscribeTradeResponse> responseObserver;
    private final Set<Smart.Account> accounts;
    private Future<?> future;

    private boolean isActive;


    public GrpcFollowerDrone(TradeDealingManager dealingManager, TradeStrategy strategy,
                             Smart.Security security, StreamObserver<Smart.SubscribeTradeResponse> responseObserver) {
        this.dealingManager = dealingManager;
        this.strategy = strategy;
        this.security = security;
        this.responseObserver = responseObserver;
        accounts = new HashSet<>();
    }


    @Override
    public void launch() {
        byte topicality;
        boolean dealing;
        Duration duration = defineInterval(strategy.timeFrame());
        do {
            topicality = strategy.analysing();
            if (topicality >= 1) {
                if (duration.toMillis() > 0) {
                    try {
                        synchronized (this) {
                            this.wait(duration.toMillis());
                        }
                    } catch (InterruptedException e) {
                        error(e);
                        break;
                    }
                }
                isActive = true;
            } else {
                do {
                    dealing = strategy.observe();
                    if (dealing) {
                        NewOrderCriteria newOrder = buildNewOrderCriteria(
                                strategy.getPrice(), strategy.getQuantity(), strategy.getDirection());
                        dealingManager.initiateNewOrder(accounts, newOrder);
                        //TODO notify
                        strategy.manageDeal();
                        //TODO deal managing
                    } else {
                        try {
                            synchronized (this) {
                                this.wait(Duration.ofSeconds(1).toMillis());
                            }
                        } catch (InterruptedException e) {
                            error(e);
                            break;
                        }
                        topicality = strategy.analysing();
                        isActive = true;
                    }
                } while (topicality < 1);
            }
        } while (isActive);

    }

    @Override
    public void addAccount(Smart.Account account) {
        accounts.add(account);
    }

    @Override
    public void removeAccount(Smart.Account account) {
        accounts.remove(account);
    }

    @Override
    public void addConsumer(Object o) {
        addAccount((Smart.Account) o);
    }

    @Override
    public void removeConsumer(Object o) {
        removeAccount((Smart.Account) o);
    }

    @Override
    public boolean isUseless() {
        return accounts.isEmpty();
    }

    @Override
    public void run() {
        launch();
    }

    @Override
    public boolean stop() {
        if (!future.isDone()) {
            future.cancel(true);
        }
        strategy.removeConsumer(this);
        if (strategy.isUseless()) {
            strategy.stop();
        }
        isActive = false;
        return true;
    }

    public Future<?> getFuture() {
        return future;
    }

    public void setFuture(Future<?> future) {
        this.future = future;
    }


    private Duration defineInterval(TimeFrame.Scope timeFrame) {
        if (timeFrame.getClass().equals(TimeFrame.IntraDay.class)) {
            TimeFrame.IntraDay intraDay = (TimeFrame.IntraDay) timeFrame;
            return switch (intraDay) {
                case M1, M5 -> Duration.ofSeconds(1);
                case M15 -> Duration.ofMinutes(1);
                default -> throw new IllegalStateException("Unexpected value: " + timeFrame);
            };
        } else {
            TimeFrame.Day day = (TimeFrame.Day) timeFrame;
            return switch (day) {
                case D1 -> Duration.ofHours(1);
                case W1 -> Duration.ofDays(1);
                default -> throw new IllegalStateException("Unexpected value: " + timeFrame);
            };
        }
    }

    private NewOrderCriteria buildNewOrderCriteria(double price, int quantity, Direction direction) {
        return new NewOrderCriteria(
                security.getTicker(),
                Board.valueOf(security.getBoard()),
                price,
                quantity,
                direction);
    }

    private void error(Exception e) {
        //TODO loggers
        //TODO send exception
        Smart.Exception ex = Smart.Exception.newBuilder()
                .setMessage(e.getMessage())
                .build();
        Smart.SubscribeTradeResponse response = Smart.SubscribeTradeResponse.newBuilder()
                .setException(ex)
                .build();
        responseObserver.onNext(response);
        responseObserver.onError(e);
        responseObserver.onCompleted();
        stop();
    }
}
