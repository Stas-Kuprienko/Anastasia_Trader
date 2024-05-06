/*
 * Stanislav Kuprienko *** Omsk, Russia
 */

/*
 * Stanislav Kuprienko *** Omsk, Russia
 */

package com.stanislav.smart.domain.analysis;

import com.stanislav.smart.domain.analysis.technical.SimpleMovingAverageAide;
import com.stanislav.smart.domain.entities.Board;
import com.stanislav.smart.domain.entities.TimeFrame;
import com.stanislav.smart.domain.entities.candles.DayCandles;
import com.stanislav.smart.domain.entities.candles.IntraDayCandles;
import com.stanislav.smart.domain.market.MarketDataProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class AideSupplier {

    private static final int SMA_CANDLE_COUNT = 60;

    private final ConcurrentHashMap<String, AnalysisAide> analyticStorage;
    private final MarketDataProvider marketDataProvider;

    @Autowired
    public AideSupplier(MarketDataProvider marketDataProvider) {
        this.marketDataProvider = marketDataProvider;
        this.analyticStorage = new ConcurrentHashMap<>();
    }


    public AnalysisAide simpleMovingAverage(String ticker, Board board, TimeFrame.Day timeFrame, int period) {
        SimpleMovingAverageAide sma = getSimpleMovingAverageAideIfExists(ticker, timeFrame, period);
        if (sma != null) return sma;

        DayCandles candles = marketDataProvider.getDayCandles(
                ticker, board, timeFrame, LocalDate.now(), SMA_CANDLE_COUNT);
        sma = new SimpleMovingAverageAide(timeFrame, candles, period);
        analyticStorage.put(ticker, sma);
        return sma;
    }

    public AnalysisAide simpleMovingAverage(String ticker, Board board, TimeFrame.IntraDay timeFrame, int period) {
        SimpleMovingAverageAide sma = getSimpleMovingAverageAideIfExists(ticker, timeFrame, period);
        if (sma != null) return sma;

        IntraDayCandles candles = marketDataProvider.getIntraDayCandles(
                ticker, board, timeFrame, LocalDateTime.now(), SMA_CANDLE_COUNT);
        sma = new SimpleMovingAverageAide(timeFrame, candles, period);
        analyticStorage.put(ticker, sma);
        return sma;
    }


    private SimpleMovingAverageAide getSimpleMovingAverageAideIfExists(String ticker,
                                                                       TimeFrame.Scope timeFrame,
                                                                       int period) {
        AnalysisAide analysisAide = analyticStorage.get(ticker);
        if (analysisAide != null) {
            if (analysisAide.getClass().equals(SimpleMovingAverageAide.class)) {
                SimpleMovingAverageAide sma = (SimpleMovingAverageAide) analysisAide;
                if (sma.getTimeFrame().equals(timeFrame) && sma.getPeriod() == period) {
                    return sma;
                }
            }
        }
        return null;
    }
}