package com.stanislav.ui.service;

import com.stanislav.ui.model.market.Futures;
import com.stanislav.ui.model.market.Stock;
import java.util.List;

public interface MarketDataService {

    Stock getStock(String ticker);

    List<Stock> getStocks();

    List<Stock> getStocks(SortByColumn sortByColumn, SortOrder sortOrder);

    Futures getFutures(String ticker);

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
