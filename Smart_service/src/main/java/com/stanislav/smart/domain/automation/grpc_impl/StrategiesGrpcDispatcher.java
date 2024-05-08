/*
 * Stanislav Kuprienko *** Omsk, Russia
 */

package com.stanislav.smart.domain.automation.grpc_impl;

import com.stanislav.smart.domain.analysis.technical.AnalysisAideSupplier;
import com.stanislav.smart.domain.analysis.technical.SimpleMovingAverageAide;
import com.stanislav.smart.domain.automation.impl.MovingAverageCrossingStrategy;
import com.stanislav.smart.domain.entities.Board;
import com.stanislav.smart.domain.entities.TimeFrame;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import stanislav.anastasia.trade.Smart;

@Component
public class StrategiesGrpcDispatcher {

    private final AnalysisAideSupplier analysisAideSupplier;

    @Autowired
    public StrategiesGrpcDispatcher(AnalysisAideSupplier analysisAideSupplier) {
        this.analysisAideSupplier = analysisAideSupplier;
    }


    public MovingAverageCrossingStrategy simpleMovingAverageCrossingStrategy(Smart.SubscribeTradeRequest request) {
        if (!request.hasStrategy() || !request.getStrategy().hasSimpleMovingAverageCrossing()) {
            throw new IllegalArgumentException(request.getAllFields().toString());
        }
        Smart.SimpleMovingAverageCrossing strategyRequest = request.getStrategy().getSimpleMovingAverageCrossing();
        SimpleMovingAverageAide sma = analysisAideSupplier.simpleMovingAverage(
                request.getSecurity().getTicker(),
                Board.valueOf(request.getSecurity().getBoard()),
                timeFrameParser(strategyRequest.getTimeFrame()),
                strategyRequest.getPeriod());
        return new MovingAverageCrossingStrategy(sma);
    }



    private TimeFrame.Scope timeFrameParser(Smart.TimeFrame timeFrameProto) {
        TimeFrame.Scope timeFrame;
        switch (timeFrameProto) {
            case D1 -> timeFrame = TimeFrame.Day.D1;
            case W1 -> timeFrame = TimeFrame.Day.W1;
            case H1 -> timeFrame = TimeFrame.IntraDay.H1;
            case M15 -> timeFrame = TimeFrame.IntraDay.M15;
            case M5 -> timeFrame = TimeFrame.IntraDay.M5;
            default -> throw new IllegalArgumentException("time_frame=" + timeFrameProto);
        }
        return timeFrame;
    }
}