/*
 * Stanislav Kuprienko *** Omsk, Russia
 */

/*
 * Stanislav Kuprienko *** Omsk, Russia
 */

package com.anastasia.smart.domain.analysis.technical.sma;

import com.anastasia.smart.domain.analysis.AnalysisAideSupplier;
import com.anastasia.smart.entities.Board;
import com.anastasia.smart.entities.TimeFrame;
import com.anastasia.smart.entities.candles.PriceCandleBox;
import com.anastasia.smart.domain.market.MarketDataProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.HashMap;

@Component("simpleMovingAverageSupplier")
public class SimpleMovingAverageSupplier implements AnalysisAideSupplier<SimpleMovingAverageAide> {

    private static final int MOVING_AVERAGE_COUNT = 60;

    private final MarketDataProvider marketDataProvider;
    private final HashMap<String, SimpleMovingAverageAide> analyticsMap;

    @Autowired
    public SimpleMovingAverageSupplier(MarketDataProvider marketDataProvider) {
        this.marketDataProvider = marketDataProvider;
        this.analyticsMap = new HashMap<>();
    }


    @Override
    public SimpleMovingAverageAide simpleMovingAverage(String ticker, Board board, TimeFrame.Scope timeFrame, int period) {
        synchronized (analyticsMap) {
            SimpleMovingAverageAide sma = analyticsMap.get(ticker);
            if (sma != null) {
                if (sma.getTimeFrame().equals(timeFrame) && sma.getPeriod() == period) {
                    return sma;
                }
            }
            PriceCandleBox candles = marketDataProvider.getLastNumberOfCandles(ticker, board, timeFrame, MOVING_AVERAGE_COUNT);
            sma = new SimpleMovingAverageAide(timeFrame, candles.candles(), period);
            analyticsMap.put(ticker, sma);
            return sma;
        }
    }
}