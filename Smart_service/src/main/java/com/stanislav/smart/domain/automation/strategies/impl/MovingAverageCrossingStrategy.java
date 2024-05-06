/*
 * Stanislav Kuprienko *** Omsk, Russia
 */

package com.stanislav.smart.domain.automation.strategies.impl;

import com.stanislav.smart.domain.analysis.technical.SimpleMovingAverageAide;
import com.stanislav.smart.domain.automation.strategies.TradingStrategy;
import com.stanislav.smart.domain.entities.Direction;

public class MovingAverageCrossingStrategy implements TradingStrategy {

    private final SimpleMovingAverageAide smaAide;
    private Direction direction;
    private byte topicality;


    public MovingAverageCrossingStrategy(SimpleMovingAverageAide smaAide) {
        this.smaAide = smaAide;
    }


    @Override
    public void analysing(double lastPrice) {
        double lastAveragePrice = smaAide.last();
        double difference = lastAveragePrice - lastPrice;
        if (difference > 0) {
            direction = Direction.Buy;
        } else if (difference < 0) {
            direction = Direction.Sell;
            difference *= (-1);
        }
        topicality = (byte) ((difference * 100) / lastPrice);
    }

    private void follow(double lastPrice) {
        double lastAveragePrice = smaAide.last();
        double difference;
        if (direction.equals(Direction.Buy)) {
            difference = lastAveragePrice - lastPrice;
        } else if (direction.equals(Direction.Sell)) {
            difference = lastPrice - lastAveragePrice;
        } else {
            throw new IllegalStateException("direction=" + direction);
        }
        if (difference < 0) {

        }
    }
}