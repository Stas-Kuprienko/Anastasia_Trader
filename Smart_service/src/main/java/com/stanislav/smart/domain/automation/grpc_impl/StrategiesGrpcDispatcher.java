/*
 * Stanislav Kuprienko *** Omsk, Russia
 */

package com.stanislav.smart.domain.automation.grpc_impl;

import com.stanislav.smart.domain.analysis.technical.AnalysisAideSupplier;
import com.stanislav.smart.domain.automation.TradingStrategy;
import com.stanislav.smart.domain.automation.impl.MovingAverageCrossingStrategy;
import com.stanislav.smart.domain.entities.Board;
import com.stanislav.smart.domain.entities.Security;
import com.stanislav.smart.domain.entities.TimeFrame;
import com.stanislav.smart.domain.market.event_stream.EventStream;
import com.stanislav.smart.domain.market.event_stream.EventStreamListener;
import stanislav.anastasia.trade.Smart;

public class StrategiesGrpcDispatcher {

    private final AnalysisAideSupplier analysisAideSupplier;
    private final EventStream eventStream;

    public StrategiesGrpcDispatcher(AnalysisAideSupplier analysisAideSupplier, EventStream eventStream) {
        this.analysisAideSupplier = analysisAideSupplier;
        this.eventStream = eventStream;
    }

    
    public TradingStrategy recognizeStrategy(Smart.SubscribeTradeRequest request) {
        Smart.Strategy protoStrategy = request.getStrategy();

        if (protoStrategy.hasSimpleMovingAverageCrossing()) {
            return movingAverageCrossing(request, protoStrategy);

        } else if (protoStrategy.hasSupportResistanceLevels()) {
            //TODO
            return null;

        } else {
            throw new IllegalArgumentException(protoStrategy.getAllFields().toString());
        }
    }


    private MovingAverageCrossingStrategy movingAverageCrossing(Smart.SubscribeTradeRequest request,
                                                                Smart.Strategy protoStrategy) {
        Smart.SimpleMovingAverageCrossing sma = protoStrategy.getSimpleMovingAverageCrossing();
        Security security = new Security(
                request.getSecurity().getTicker(),Board.valueOf(request.getSecurity().getBoard()));

        var collector = eventStream.getEventStream(security).getCollector();
        if (collector == null) {
            EventStreamListener streamListener = eventStream.subscribe(security);
            collector = streamListener.getCollector();
        }
        return new MovingAverageCrossingStrategy(
                analysisAideSupplier.simpleMovingAverage(
                        security.ticker(),
                        security.board(),
                        timeFrameParser(sma.getTimeFrame()),
                        sma.getPeriod()),
                collector);
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