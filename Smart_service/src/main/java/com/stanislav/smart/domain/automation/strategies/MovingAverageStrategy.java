/*
 * Stanislav Kuprienko *** Omsk, Russia
 */

package com.stanislav.smart.domain.automation.strategies;

import com.stanislav.smart.domain.analysis.technical.SimpleMovingAverageAide;
import com.stanislav.smart.domain.automation.TradingStrategy;
import com.stanislav.smart.domain.entities.Direction;
import com.stanislav.smart.domain.entities.TimeFrame;
import com.stanislav.smart.domain.market.event_stream.EventStreamListener;

public class MovingAverageStrategy implements TradingStrategy {

    private final SimpleMovingAverageAide smaAide;
    private final EventStreamListener.OrderBookCollector collector;

    private Direction direction;


    public MovingAverageStrategy(SimpleMovingAverageAide smaAide, EventStreamListener.EventCollector collector) {
        this.smaAide = smaAide;
        this.collector = (EventStreamListener.OrderBookCollector) collector;
    }


    @Override
    public TimeFrame.Scope timeFrame() {
        return smaAide.getTimeFrame();
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

//TODO !!!!!!!!!!!!

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


    private boolean deciding() {
        //TODO
        return false;
    }
}