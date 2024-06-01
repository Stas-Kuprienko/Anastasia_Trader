/*
 * Stanislav Kuprienko *** Omsk, Russia
 */

package com.stanislav.trade.domain.service;

import com.stanislav.trade.entities.markets.Futures;
import com.stanislav.trade.entities.markets.Stock;

import java.util.List;
import java.util.Optional;

public interface ExchangeData {

    Optional<Stock> getStock(String ticker);

    List<Stock> getStocks();

    List<Stock> getStocks(SortByColumn sortByColumn, SortOrder sortOrder);

    Optional<Futures> getFutures(String ticker);

    List<Futures> getFuturesList();

    List<Futures> getFuturesList(SortByColumn sortByColumn, SortOrder sortOrder);

    enum SortByColumn {

        NONE,
        TICKER,
        TRADE_VOLUME,
        CHANGE_PERCENT
    }

    enum SortOrder {

        asc, desc
    }
}