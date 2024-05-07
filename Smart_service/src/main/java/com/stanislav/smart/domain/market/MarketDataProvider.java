package com.stanislav.smart.domain.market;

import com.stanislav.smart.domain.entities.Board;
import com.stanislav.smart.domain.entities.TimeFrame;
import com.stanislav.smart.domain.entities.candles.Candles;
import com.stanislav.smart.domain.entities.candles.DayCandles;
import com.stanislav.smart.domain.entities.candles.IntraDayCandles;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface MarketDataProvider {

    Candles getLastNumberOfCandles(String ticker, Board board, TimeFrame.Scope timeFrame, Integer count);

    DayCandles getDayCandles(String ticker, Board board, TimeFrame.Scope timeFrame, LocalDate to, Integer count);

    DayCandles getDayCandles(String ticker, Board board, TimeFrame.Scope timeFrame, LocalDate from, LocalDate to);

    IntraDayCandles getIntraDayCandles(String ticker, Board board, TimeFrame.Scope timeFrame, LocalDateTime to, Integer count);

    IntraDayCandles getIntraDayCandles(String ticker, Board board, TimeFrame.Scope timeFrame, LocalDateTime from, LocalDateTime to);

    DayCandles getStockDayCandles(String ticker, TimeFrame.Day timeFrame, LocalDate to, Integer count);

    DayCandles getStockDayCandles(String ticker, TimeFrame.Day timeFrame, LocalDate from, LocalDate to);

    IntraDayCandles getStockIntraDayCandles(String ticker, TimeFrame.IntraDay timeFrame, LocalDateTime to, Integer count);

    IntraDayCandles getStockIntraDayCandles(String ticker, TimeFrame.IntraDay timeFrame, LocalDateTime from, LocalDateTime to);

    DayCandles getFuturesDayCandles(String ticker, TimeFrame.Day timeFrame, LocalDate to, Integer count);

    DayCandles getFuturesDayCandles(String ticker, TimeFrame.Day timeFrame, LocalDate from, LocalDate to);

    IntraDayCandles getFuturesIntraDayCandles(String ticker, TimeFrame.IntraDay timeFrame, LocalDateTime to, Integer count);

    IntraDayCandles getFuturesIntraDayCandles(String ticker, TimeFrame.IntraDay timeFrame, LocalDateTime from, LocalDateTime to);
}
