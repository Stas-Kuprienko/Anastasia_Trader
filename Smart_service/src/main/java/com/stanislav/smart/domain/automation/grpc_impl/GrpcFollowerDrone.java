package com.stanislav.smart.domain.automation.grpc_impl;

import com.stanislav.smart.domain.automation.Drone;
import com.stanislav.smart.domain.automation.TradingStrategy;
import io.grpc.stub.StreamObserver;
import stanislav.anastasia.trade.Smart;
import java.time.Duration;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ScheduledFuture;

import static com.stanislav.smart.domain.entities.TimeFrame.*;

public class GrpcFollowerDrone implements Drone {

    private final TradingStrategy strategy;
    private final Smart.SubscribeTradeRequest request;
    private final StreamObserver<Smart.SubscribeTradeResponse> responseObserver;
    private final Set<Smart.Account> accounts;
    private ScheduledFuture<?> scheduledFuture;

    private boolean isActive;


    public GrpcFollowerDrone(TradingStrategy strategy, Smart.SubscribeTradeRequest request,
                             StreamObserver<Smart.SubscribeTradeResponse> responseObserver) {
        this.strategy = strategy;
        this.request = request;
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
                        //TODO trade order
                        //TODO notify
                        strategy.manageDeal();
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
    public ScheduledFuture<?> getScheduledFuture() {
        return scheduledFuture;
    }

    @Override
    public void setScheduledFuture(ScheduledFuture<?> scheduledFuture) {
        this.scheduledFuture = scheduledFuture;
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
                case M1, M5 -> duration = Duration.ofSeconds(1);
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
        Smart.Exception ex = Smart.Exception.newBuilder()
                .setMessage(e.getMessage())
                .build();
        Smart.SubscribeTradeResponse response = Smart.SubscribeTradeResponse.newBuilder()
                .setException(ex)
                .build();
        responseObserver.onNext(response);
        responseObserver.onError(e);
    }
}
