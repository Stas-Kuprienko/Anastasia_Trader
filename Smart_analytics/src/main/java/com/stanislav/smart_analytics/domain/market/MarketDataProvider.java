package com.stanislav.smart_analytics.domain.market;

import com.stanislav.smart_analytics.domain.entities.Board;
import com.stanislav.smart_analytics.domain.entities.TimeFrame;
import com.stanislav.smart_analytics.domain.entities.candles.DayCandles;
import com.stanislav.smart_analytics.domain.entities.candles.IntraDayCandles;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface MarketDataProvider {

    DayCandles getDayCandles(String ticker, Board board, TimeFrame.Day timeFrame,
                             LocalDate from, LocalDate to, Integer count);

    DayCandles getDayCandles(String ticker, Board board,
                             TimeFrame.Day timeFrame, LocalDate from, LocalDate to);

    IntraDayCandles getIntraDayCandles(String ticker, Board board, TimeFrame.IntraDay timeFrame,
                                       LocalDateTime from, LocalDateTime to, Integer count);

    IntraDayCandles getIntraDayCandles(String ticker, Board board,
                                       TimeFrame.IntraDay timeFrame, LocalDateTime from, LocalDateTime to);
}
