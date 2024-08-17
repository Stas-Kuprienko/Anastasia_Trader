package com.anastasia.ui.service;

import com.anastasia.ui.model.ExchangeMarket;
import com.anastasia.ui.model.market.Futures;
import com.anastasia.ui.model.market.Stock;
import java.util.List;

public interface MarketDataService {

    Stock getStock(long userId, ExchangeMarket exchangeMarket, String ticker);

    List<Stock> getStocks(long userId, ExchangeMarket exchangeMarket);

    List<Stock> getStocks(long userId, ExchangeMarket exchangeMarket, SortByColumn sortByColumn, SortOrder sortOrder);

    Futures getFutures(long userId, ExchangeMarket exchangeMarket, String ticker);

    List<Futures> getFuturesList(long userId, ExchangeMarket exchangeMarket);

    List<Futures> getFuturesList(long userId, ExchangeMarket exchangeMarket, SortByColumn sortByColumn, SortOrder sortOrder);


    enum SortByColumn {
        NONE,
        TRADE_VOLUME,
    }

    enum SortOrder {
        asc,
        desc
    }
}
