package com.stanislav.smart.domain.automation.drones;

import com.stanislav.smart.domain.automation.strategies.TradingStrategy;
import com.stanislav.smart.domain.market.event_stream.EventStreamListener;
import stanislav.anastasia.trade.Smart;

import java.util.concurrent.ConcurrentHashMap;

public class GrpcFollowerDrone implements Drone {

    private final ConcurrentHashMap<Integer, Smart.Account> followers;
    private final TradingStrategy strategy;
    private final EventStreamListener.OrderBookCollector collector;

    public GrpcFollowerDrone(TradingStrategy strategy, EventStreamListener.OrderBookCollector collector) {
        this.followers = new ConcurrentHashMap<>();
        this.strategy = strategy;
        this.collector = collector;
    }

    @Override
    public void launch() {

    }

    @Override
    public void following() {
        double lastAskPrice = collector.lastAskPrice();
        strategy.analysing(lastAskPrice);
    }


    @Override
    public void run() {
        following();
    }

    @Override
    public void stop() {

    }
}
