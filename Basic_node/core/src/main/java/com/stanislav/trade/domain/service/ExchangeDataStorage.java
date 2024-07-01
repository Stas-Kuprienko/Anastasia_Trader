package com.stanislav.trade.domain.service;

import com.stanislav.trade.entities.markets.Futures;
import com.stanislav.trade.entities.markets.Stock;

import java.util.List;
import java.util.Optional;

public interface ExchangeDataStorage {

    void add(Stock stock);

    void addStockList(List<Stock> stocks);

    Optional<Stock> getStock(String ticker);

    List<Stock> getAllStocks();

    void add(Futures futures);

    void addFuturesList(List<Futures> futuresList);

    Optional<Futures> getFutures(String ticker);

    List<Futures> getAllFutures();
}
