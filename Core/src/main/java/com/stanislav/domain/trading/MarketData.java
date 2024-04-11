package com.stanislav.domain.trading;

import com.stanislav.entities.candles.DayCandles;
import com.stanislav.entities.candles.IntraDayCandles;
import com.stanislav.entities.markets.Stock;
import com.stanislav.entities.user.Account;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface MarketData {

    Stock getStock(Account account, String ticker);

    List<Stock> getStocks(Account account);

    DayCandles getDayCandles(Account account, String ticker, String timeFrame, LocalDate from, LocalDate to, Integer interval);

    IntraDayCandles getIntraDayCandles(Account account, String ticker, String timeFrame, LocalDateTime from, LocalDateTime to, Integer interval);
}