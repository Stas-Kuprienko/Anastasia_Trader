package org.stanislav.domain.trading;

import org.stanislav.entities.candles.DayCandles;
import org.stanislav.entities.candles.IntraDayCandles;
import org.stanislav.entities.markets.Stock;
import org.stanislav.entities.user.Account;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface MarketData {

    Stock getStock(Account account, String ticker);

    List<Stock> getStocks(Account account);

    DayCandles getDayCandles(Account account, String ticker, String timeFrame, LocalDate from, LocalDate to, Integer interval);

    IntraDayCandles getIntraDayCandles(Account account, String ticker, String timeFrame, LocalDateTime from, LocalDateTime to, Integer interval);
}