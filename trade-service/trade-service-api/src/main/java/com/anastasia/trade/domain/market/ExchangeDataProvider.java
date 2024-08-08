/*
 * Stanislav Kuprienko *** Omsk, Russia
 */

package com.anastasia.trade.domain.market;

import com.anastasia.trade.entities.ExchangeMarket;
import com.anastasia.trade.entities.markets.Futures;
import com.anastasia.trade.entities.markets.Stock;

import java.util.List;
import java.util.Optional;

public interface ExchangeDataProvider {

    ExchangeMarket getExchange();

    Optional<Stock> getStock(String ticker);

    List<Stock> getStocks();

    List<Stock> getStocks(SortByColumn sortByColumn, SortOrder sortOrder);

    Optional<Futures> getFutures(String ticker);

    List<Futures> getFuturesList();

    List<Futures> getFuturesList(SortByColumn sortByColumn, SortOrder sortOrder);

    enum SortByColumn {

        NONE,
        TRADE_VOLUME,
    }

    enum SortOrder {

        asc,
        desc
    }
}