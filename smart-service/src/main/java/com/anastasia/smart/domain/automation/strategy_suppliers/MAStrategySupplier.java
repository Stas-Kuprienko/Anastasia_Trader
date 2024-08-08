package com.anastasia.smart.domain.automation.strategy_suppliers;

import com.anastasia.smart.domain.analysis.technical.sma.SimpleMovingAverageAide;
import com.anastasia.smart.domain.analysis.technical.sma.SimpleMovingAverageSupplier;
import com.anastasia.smart.domain.automation.TradeStrategySupplier;
import com.anastasia.smart.domain.automation.strategies.MovingAverageStrategy;
import com.anastasia.smart.domain.market.event_stream.OrderBookStream;
import com.anastasia.smart.entities.Board;
import com.anastasia.smart.entities.TimeFrame;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import stanislav.anastasia.trade.Smart;
import java.util.HashMap;

@Component
public class MAStrategySupplier implements TradeStrategySupplier<MovingAverageStrategy> {

    public static final int MA_PERIOD = 20;

    private final SimpleMovingAverageSupplier simpleMovingAverageSupplier;
    private final OrderBookStream orderBookStream;
    private final HashMap<Smart.Security, MovingAverageStrategy> strategyMap;


    @Autowired
    public MAStrategySupplier(SimpleMovingAverageSupplier simpleMovingAverageSupplier, OrderBookStream orderBookStream) {
        this.simpleMovingAverageSupplier = simpleMovingAverageSupplier;
        strategyMap = new HashMap<>();
        this.orderBookStream = orderBookStream;
    }


    @Override
    public MovingAverageStrategy supply(Smart.Security security, TimeFrame.Scope timeFrame) {
        synchronized (strategyMap) {
            var strategy = strategyMap.get(security);
            if (strategy == null || !strategy.timeFrame().equals(timeFrame)) {
                SimpleMovingAverageAide sma = simpleMovingAverageSupplier.simpleMovingAverage(
                        security.getTicker(),
                        Board.valueOf(security.getBoard()),
                        timeFrame,
                        MA_PERIOD);
                var listener = orderBookStream.getListener(security);
                if (listener == null) {
                    listener = orderBookStream.subscribe(security);
                }
                strategy = new MovingAverageStrategy(sma, listener);
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
