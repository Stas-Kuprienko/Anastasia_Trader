/*
 * Stanislav Kuprienko *** Omsk, Russia
 */

package com.anastasia.smart.domain.automation.strategies;

import com.anastasia.smart.domain.automation.Drone;
import com.anastasia.smart.domain.automation.TradeStrategy;
import com.anastasia.smart.domain.market.event_stream.OrderBookStreamListener;
import com.anastasia.smart.domain.analysis.technical.sma.SimpleMovingAverageAide;
import com.anastasia.smart.entities.Direction;
import com.anastasia.smart.entities.TimeFrame;

import java.util.HashSet;
import java.util.Set;

public class MovingAverageStrategy implements TradeStrategy {

    private final Set<Drone> consumers;
    private final SimpleMovingAverageAide smaAide;
    private final OrderBookStreamListener listener;
    private final OrderBookStreamListener.OrderBookCollector collector;

    private Direction direction;


    public MovingAverageStrategy(SimpleMovingAverageAide smaAide, OrderBookStreamListener listener) {
        this.smaAide = smaAide;
        this.listener = listener;
        this.collector = listener.orderBookCollector();
        consumers = new HashSet<>();
    }

    @Override
    public void addConsumer(Object o) {
        consumers.add((Drone) o);
    }

    @Override
    public void removeConsumer(Object o) {
        consumers.remove((Drone) o);
    }

    @Override
    public boolean isUseless() {
        return consumers.isEmpty();
    }

    @Override
    public TimeFrame.Scope timeFrame() {
        return smaAide.getTimeFrame();
    }

    @Override
    public double getPrice() {
        return collector.lastAskPrice();
    }

    @Override
    public int getQuantity() {
        //TODO
        return 0;
    }

    @Override
    public byte analysing() {
        double lastAveragePrice = smaAide.last();
        double lastPrice = collector.lastAskPrice();
        smaAide.update(lastPrice);
        double difference = lastAveragePrice - lastPrice;
        if (difference > 0) {
            direction = Direction.Buy;
        } else if (difference < 0) {
            direction = Direction.Sell;
            difference *= (-1);
        }
        return (byte) ((difference * 100) / lastPrice);
    }


    @Override
    public boolean observe() {
        double lastPrice = collector.currentAsk().getPrice();
        double lastAveragePrice = smaAide.last();
        double difference;
        if (direction.equals(Direction.Buy)) {
            difference = lastAveragePrice - lastPrice;
        } else if (direction.equals(Direction.Sell)) {
            difference = lastPrice - lastAveragePrice;
        } else {
            throw new IllegalStateException("direction=" + direction);
        }
        if (difference <= 0) {
            return deciding();
        }
        return false;
    }

    @Override
    public void manageDeal() {
        //TODO
    }

    @Override
    public Direction getDirection() {
        return direction;
    }

    @Override
    public boolean stop() {
        listener.removeConsumer(this);
        if (listener.isUseless()) {
            return listener.stop();
        }
        return false;
    }

    private boolean deciding() {
        //TODO
        return false;
    }
}