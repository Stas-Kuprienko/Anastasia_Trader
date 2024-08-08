package com.anastasia.ui.service;

import com.anastasia.ui.model.ExchangeMarket;
import com.anastasia.ui.model.market.Futures;
import com.anastasia.ui.model.market.Stock;
import java.util.List;

public interface MarketDataService {

    Stock getStock(ExchangeMarket exchangeMarket, String ticker);

    List<Stock> getStocks(ExchangeMarket exchangeMarket);

    List<Stock> getStocks(ExchangeMarket exchangeMarket, SortByColumn sortByColumn, SortOrder sortOrder);

    Futures getFutures(ExchangeMarket exchangeMarket, String ticker);

    List<Futures> getFuturesList(ExchangeMarket exchangeMarket);

    List<Futures> getFuturesList(ExchangeMarket exchangeMarket, SortByColumn sortByColumn, SortOrder sortOrder);


    enum SortByColumn {
        NONE,
        TRADE_VOLUME,
    }

    enum SortOrder {
        asc,
        desc
    }
}
