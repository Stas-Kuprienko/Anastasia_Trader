package com.stanislav.smart.domain.analysis.technical;

import com.stanislav.smart.domain.analysis.AnalysisAide;
import com.stanislav.smart.domain.entities.TimeFrame;
import com.stanislav.smart.domain.entities.candles.Candles;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

@Component
public class AnalysisAideSupplier {

    private final ConcurrentHashMap<String, AnalysisAide> analyticsMap;

    public AnalysisAideSupplier() {
        this.analyticsMap = new ConcurrentHashMap<>();
    }


    public SimpleMovingAverageAide simpleMovingAverage(String ticker, Candles candles, TimeFrame.Scope timeFrame, int period) {
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
        sma = new SimpleMovingAverageAide(timeFrame, candles, period);
        analyticsMap.put(ticker, sma);
        return sma;
    }
}
