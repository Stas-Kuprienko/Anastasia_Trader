package com.stanislav.smart.domain.automation.grpc_impl;

import com.stanislav.smart.domain.automation.Drone;
import com.stanislav.smart.domain.automation.TradingStrategy;
import com.stanislav.smart.domain.entities.Security;
import io.grpc.stub.StreamObserver;
import stanislav.anastasia.trade.Smart;
import java.time.Duration;
import java.util.concurrent.ScheduledFuture;

import static com.stanislav.smart.domain.entities.TimeFrame.*;

public class GrpcFollowerDrone implements Drone {

    private final Security security;
    private final TradingStrategy strategy;
    private final Smart.SubscribeTradeRequest request;
    private final StreamObserver<Smart.SubscribeTradeResponse> responseObserver;
    private ScheduledFuture<?> scheduledFuture;

    private boolean isActive;


    public GrpcFollowerDrone(Security security, TradingStrategy strategy,
                             Smart.SubscribeTradeRequest request,
                             StreamObserver<Smart.SubscribeTradeResponse> responseObserver) {
        this.security = security;
        this.strategy = strategy;
        this.request = request;
        this.responseObserver = responseObserver;
    }


    @Override
    public void launch() {
        byte topicality;
        boolean isOrder;
        Duration duration = defineInterval(strategy.timeFrame());
        do {
            topicality = strategy.analysing();
            if (topicality >= 1) {
                if (!duration.isZero()) {
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
                    isOrder = strategy.observe();
                    if (isOrder) {
                        Smart.OrderNotification notification = Smart.OrderNotification.newBuilder()
                                .setStrategy(request.getStrategy())
                                .setSecurity(request.getSecurity())
                                .setPrice(0).build();
                        Smart.SubscribeTradeResponse subscribeTradeResponse = Smart.SubscribeTradeResponse.newBuilder()
                                .setNotification(notification).build();
                        responseObserver.onNext(subscribeTradeResponse);
                        strategy.manageDeal();
                    }
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
                } while (topicality < 1);
            }
        } while (isActive);

    }

    @Override
    public Security getSecurity() {
        return security;
    }

    @Override
    public TradingStrategy getStrategy() {
        return strategy;
    }

    @Override
    public ScheduledFuture<?> getScheduledFuture() {
        return scheduledFuture;
    }

    @Override
    public void setScheduledFuture(ScheduledFuture<?> scheduledFuture) {
        this.scheduledFuture = scheduledFuture;
    }

    @Override
    public void run() {
        launch();
    }

    @Override
    public boolean stop() {
        isActive = false;
        if (!scheduledFuture.isDone() || !scheduledFuture.isCancelled()) {
            return scheduledFuture.cancel(true);
        }
        return true;
    }


    private Duration defineInterval(Scope timeFrame) {
        Duration duration;
        if (timeFrame.getClass().equals(IntraDay.class)) {
            IntraDay intraDay = (IntraDay) timeFrame;
            switch (intraDay) {
                case M1, M5 -> duration = Duration.ZERO;
                case M15 -> duration = Duration.ofMinutes(1);
                default -> throw new IllegalStateException("Unexpected value: " + timeFrame);
            }
        } else {
            Day day = (Day) timeFrame;
            switch (day) {
                case D1 -> duration = Duration.ofHours(1);
                case W1 -> duration = Duration.ofDays(1);
                default -> throw new IllegalStateException("Unexpected value: " + timeFrame);
            }
        }
        return duration;
    }

    private void error(Exception e) {
        isActive = false;
        //TODO loggers
        //TODO send exception
        Smart.SubscribeTradeResponse response = Smart.SubscribeTradeResponse.newBuilder()
                .build();
        responseObserver.onNext(response);
    }
}
