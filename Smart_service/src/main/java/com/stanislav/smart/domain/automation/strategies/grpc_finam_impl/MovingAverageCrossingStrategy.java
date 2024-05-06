/*
 * Stanislav Kuprienko *** Omsk, Russia
 */

package com.stanislav.smart.domain.automation.strategies.grpc_finam_impl;

import com.stanislav.smart.domain.analysis.technical.SimpleMovingAverageAide;
import com.stanislav.smart.domain.entities.Direction;
import com.stanislav.smart.domain.market.event_stream.finam.FinamOrderBookCollector;
import com.stanislav.smart.domain.automation.strategies.TradeStrategy;

public class MovingAverageCrossingStrategy implements TradeStrategy {

    private final FinamOrderBookCollector orderBookCollector;
    private final SimpleMovingAverageAide smaAide;
    private Direction direction;
    private byte topicality;


    public MovingAverageCrossingStrategy(FinamOrderBookCollector orderBookCollector, SimpleMovingAverageAide smaAide) {
        this.orderBookCollector = orderBookCollector;
        this.smaAide = smaAide;
        analysing();
    }


    @Override
    public void trading() {

        orderBookCollector.currentAsk();
    }

    private void analysing() {
        double lastAveragePrice = smaAide.last();
        double lastAskPrice = orderBookCollector.currentAsk().getPrice();
        double difference = lastAveragePrice - lastAskPrice;
        if (difference > 0) {
            direction = Direction.Buy;
        } else if (difference < 0) {
            direction = Direction.Sell;
            difference *= (-1);
        }
        topicality = (byte) ((difference * 100) / lastAskPrice);
    }

    public static class Trading implements Runnable {

        @Override
        public void run() {

        }
    }
}