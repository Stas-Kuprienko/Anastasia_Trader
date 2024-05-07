/*
 * Stanislav Kuprienko *** Omsk, Russia
 */

package com.stanislav.smart.domain.automation.impl;

import com.stanislav.smart.domain.analysis.technical.SimpleMovingAverageAide;
import com.stanislav.smart.domain.automation.TradingStrategy;
import com.stanislav.smart.domain.entities.Direction;
import com.stanislav.smart.domain.market.event_stream.OrderBookRow;

public class MovingAverageCrossingStrategy implements TradingStrategy {

    private static final int id = 0;
    private final SimpleMovingAverageAide smaAide;
    private Direction direction;


    public MovingAverageCrossingStrategy(SimpleMovingAverageAide smaAide) {
        this.smaAide = smaAide;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public byte analysing(double lastPrice) {
        double lastAveragePrice = smaAide.last();
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
    public boolean follow(OrderBookRow orderBookRow) {
        double lastPrice = orderBookRow.getPrice();
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


    private boolean deciding() {

        return false;
    }
}