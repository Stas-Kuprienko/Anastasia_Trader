package com.stanislav.smart.domain.market;

import com.stanislav.smart.domain.entities.TimeFrame;
import com.stanislav.smart.domain.entities.candles.DayCandles;
import com.stanislav.smart.domain.entities.candles.IntraDayCandles;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface MarketDataProvider {

    DayCandles getStockDayCandles(String ticker, TimeFrame.Day timeFrame, LocalDate to, Integer count);

    DayCandles getStockDayCandles(String ticker, TimeFrame.Day timeFrame, LocalDate from, LocalDate to);

    IntraDayCandles getStockIntraDayCandles(String ticker, TimeFrame.IntraDay timeFrame, LocalDateTime to, Integer count);

    IntraDayCandles getStockIntraDayCandles(String ticker, TimeFrame.IntraDay timeFrame, LocalDateTime from, LocalDateTime to);

    DayCandles getFuturesDayCandles(String ticker, TimeFrame.Day timeFrame, LocalDate to, Integer count);

    DayCandles getFuturesDayCandles(String ticker, TimeFrame.Day timeFrame, LocalDate from, LocalDate to);

    IntraDayCandles getFuturesIntraDayCandles(String ticker, TimeFrame.IntraDay timeFrame, LocalDateTime to, Integer count);

    IntraDayCandles getFuturesIntraDayCandles(String ticker, TimeFrame.IntraDay timeFrame, LocalDateTime from, LocalDateTime to);


}
