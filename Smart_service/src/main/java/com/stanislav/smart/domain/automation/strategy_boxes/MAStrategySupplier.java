package com.stanislav.smart.domain.automation.strategy_boxes;

import com.stanislav.smart.domain.analysis.technical.AnalysisAideSupplier;
import com.stanislav.smart.domain.analysis.technical.SimpleMovingAverageAide;
import com.stanislav.smart.domain.automation.TradeStrategySupplier;
import com.stanislav.smart.domain.automation.strategies.MovingAverageStrategy;
import com.stanislav.smart.domain.entities.Board;
import com.stanislav.smart.domain.entities.TimeFrame;
import com.stanislav.smart.domain.market.event_stream.EventStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import stanislav.anastasia.trade.Smart;
import java.util.HashMap;

@Component("maStrategyBox")
public class MAStrategySupplier implements TradeStrategySupplier<MovingAverageStrategy> {

    public static final int MA_PERIOD = 20;

    private final AnalysisAideSupplier analysisAideSupplier;
    private final EventStream eventStream;
    private final HashMap<Smart.Security, MovingAverageStrategy> strategyMap;


    @Autowired
    public MAStrategySupplier(AnalysisAideSupplier analysisAideSupplier,
                              @Qualifier("orderBookStream") EventStream eventStream) {
        this.analysisAideSupplier = analysisAideSupplier;
        strategyMap = new HashMap<>();
        this.eventStream = eventStream;
    }


    @Override
    public MovingAverageStrategy supply(Smart.Security security, TimeFrame.Scope timeFrame) {
        synchronized (strategyMap) {
            var strategy = strategyMap.get(security);
            if (strategy == null || !strategy.timeFrame().equals(timeFrame)) {
                SimpleMovingAverageAide sma = analysisAideSupplier.simpleMovingAverage(
                        security.getTicker(),
                        Board.valueOf(security.getBoard()),
                        timeFrame,
                        MA_PERIOD);
                var eventStreamListener = eventStream.getEventStreamListener(security);
                if (eventStreamListener == null) {
                    eventStreamListener = eventStream.subscribe(security);
                }
                strategy = new MovingAverageStrategy(sma, eventStreamListener.getCollector());
                strategyMap.put(security, strategy);
            }
            return strategy;
        }
    }

    @Override
    public String getName() {
        return MovingAverageStrategy.class.getSimpleName();
    }
}
