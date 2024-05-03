/*
 * Stanislav Kuprienko *** Omsk, Russia
 */

package com.stanislav.smart.domain.strategies.grpc_finam_impl;

import com.stanislav.smart.domain.analysis.technical.SimpleMovingAverageAide;
import com.stanislav.smart.domain.entities.Direction;
import com.stanislav.smart.domain.event_stream.finam.FinamOrderBookCollector;
import com.stanislav.smart.domain.strategies.TradeStrategy;

public class MovingAverageCrossingStrategy implements TradeStrategy {

    private final FinamOrderBookCollector orderBookCollector;
    private final SimpleMovingAverageAide smaAide;
    private Direction direction;

    public MovingAverageCrossingStrategy(FinamOrderBookCollector orderBookCollector, SimpleMovingAverageAide smaAide) {
        this.orderBookCollector = orderBookCollector;
        this.smaAide = smaAide;
    }


    public void start() {
        orderBookCollector.currentAsk();
    }

    private void analysing() {
        double lastAveragePrice = smaAide.last();
        double lastAskPrice = orderBookCollector.currentAsk().getPrice();
        double difference = lastAveragePrice - lastAskPrice;
        if (difference < 0) {
            direction = Direction.Sell;
        }
    }

    public static class Trading implements Runnable {

        @Override
        public void run() {

        }
    }
}