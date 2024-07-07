package com.stanislav.smart.domain.analysis.technical;

import com.stanislav.smart.domain.analysis.AnalysisAide;
import com.stanislav.smart.domain.entities.Board;
import com.stanislav.smart.domain.entities.TimeFrame;
import com.stanislav.smart.domain.entities.candles.PriceCandleBox;
import com.stanislav.smart.domain.market.MarketDataProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.HashMap;

@Component
public class AnalysisAideSupplier {

    private static final int MOVING_AVERAGE_COUNT = 60;

    private final MarketDataProvider marketDataProvider;
    private final HashMap<String, AnalysisAide> analyticsMap;

    @Autowired
    public AnalysisAideSupplier(MarketDataProvider marketDataProvider) {
        this.marketDataProvider = marketDataProvider;
        this.analyticsMap = new HashMap<>();
    }


    public SimpleMovingAverageAide simpleMovingAverage(String ticker, Board board, TimeFrame.Scope timeFrame, int period) {
        synchronized (analyticsMap) {
            SimpleMovingAverageAide sma;
            AnalysisAide analysisAide = analyticsMap.get(ticker);
            if (analysisAide != null) {
                if (analysisAide.getClass().equals(SimpleMovingAverageAide.class)) {
                    sma = (SimpleMovingAverageAide) analysisAide;
                    if (sma.getTimeFrame().equals(timeFrame) && sma.getPeriod() == period) {
                        return sma;
                    }
                }
            }
            PriceCandleBox candles = marketDataProvider.getLastNumberOfCandles(ticker, board, timeFrame, MOVING_AVERAGE_COUNT);
            sma = new SimpleMovingAverageAide(timeFrame, candles.candles(), period);
            analyticsMap.put(ticker, sma);
            return sma;
        }
    }
}