package com.stanislav.smart.domain.analysis.technical;

import com.stanislav.smart.domain.analysis.AnalysisAide;
import com.stanislav.smart.domain.entities.Board;
import com.stanislav.smart.domain.entities.TimeFrame;
import com.stanislav.smart.domain.entities.candles.PriceCandleBox;
import com.stanislav.smart.domain.market.MarketDataProvider;
import java.util.concurrent.ConcurrentHashMap;

public class AnalysisAideSupplier {

    private static final int MOVING_AVERAGE_COUNT = 60;

    private final MarketDataProvider marketDataProvider;
    private final ConcurrentHashMap<String, AnalysisAide> analyticsMap;

    public AnalysisAideSupplier(MarketDataProvider marketDataProvider) {
        this.marketDataProvider = marketDataProvider;
        this.analyticsMap = new ConcurrentHashMap<>();
    }


    public SimpleMovingAverageAide simpleMovingAverage(String ticker, Board board, TimeFrame.Scope timeFrame, int period) {
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