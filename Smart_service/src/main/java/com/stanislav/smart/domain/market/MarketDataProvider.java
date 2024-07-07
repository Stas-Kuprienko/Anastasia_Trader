package com.stanislav.smart.domain.market;

import com.stanislav.smart.domain.entities.Board;
import com.stanislav.smart.domain.entities.TimeFrame;
import com.stanislav.smart.domain.entities.candles.DayCandleBox;
import com.stanislav.smart.domain.entities.candles.IntraDayCandleBox;
import com.stanislav.smart.domain.entities.candles.PriceCandleBox;
import java.time.LocalDate;
import java.time.LocalDateTime;

public interface MarketDataProvider {

    PriceCandleBox getLastNumberOfCandles(String ticker, Board board, TimeFrame.Scope timeFrame, Integer count);

    DayCandleBox getDayCandles(String ticker, Board board, TimeFrame.Scope timeFrame, LocalDate to, Integer count);

    DayCandleBox getDayCandles(String ticker, Board board, TimeFrame.Scope timeFrame, LocalDate from, LocalDate to);

    IntraDayCandleBox getIntraDayCandles(String ticker, Board board, TimeFrame.Scope timeFrame, LocalDateTime to, Integer count);

    IntraDayCandleBox getIntraDayCandles(String ticker, Board board, TimeFrame.Scope timeFrame, LocalDateTime from, LocalDateTime to);

    DayCandleBox getStockDayCandles(String ticker, TimeFrame.Day timeFrame, LocalDate to, Integer count);

    DayCandleBox getStockDayCandles(String ticker, TimeFrame.Day timeFrame, LocalDate from, LocalDate to);

    IntraDayCandleBox getStockIntraDayCandles(String ticker, TimeFrame.IntraDay timeFrame, LocalDateTime to, Integer count);

    IntraDayCandleBox getStockIntraDayCandles(String ticker, TimeFrame.IntraDay timeFrame, LocalDateTime from, LocalDateTime to);

    DayCandleBox getFuturesDayCandles(String ticker, TimeFrame.Day timeFrame, LocalDate to, Integer count);

    DayCandleBox getFuturesDayCandles(String ticker, TimeFrame.Day timeFrame, LocalDate from, LocalDate to);

    IntraDayCandleBox getFuturesIntraDayCandles(String ticker, TimeFrame.IntraDay timeFrame, LocalDateTime to, Integer count);

    IntraDayCandleBox getFuturesIntraDayCandles(String ticker, TimeFrame.IntraDay timeFrame, LocalDateTime from, LocalDateTime to);
}
