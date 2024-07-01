package com.stanislav.trade.domain.service.ram;

import com.stanislav.trade.domain.service.ExchangeDataStorage;
import com.stanislav.trade.entities.markets.Futures;
import com.stanislav.trade.entities.markets.Stock;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class RamExchangeDataStorage implements ExchangeDataStorage {

    //TODO TEMPORARY SOLUTION, NEEDED TO REMAKE BY REDIS

    private final ConcurrentHashMap<String, Stock> stocksMap;
    private final ConcurrentHashMap<String, Futures> futuresMap;

    public RamExchangeDataStorage() {
        stocksMap = new ConcurrentHashMap<>();
        futuresMap = new ConcurrentHashMap<>();
    }

    @Override
    public void add(Stock stock) {
        stocksMap.put(stock.getTicker(), stock);
    }

    @Override
    public void addStockList(List<Stock> stocks) {
        stocks.forEach(s -> stocksMap.put(s.getTicker(), s));
    }

    @Override
    public Optional<Stock> getStock(String ticker) {
        return Optional.ofNullable(stocksMap.get(ticker));
    }

    @Override
    public List<Stock> getAllStocks() {
        //TODO sorting
        return stocksMap.values().stream().toList();
    }

    @Override
    public void add(Futures futures) {
        futuresMap.put(futures.getTicker(), futures);
    }

    @Override
    public void addFuturesList(List<Futures> futuresList) {
        futuresList.forEach(f -> futuresMap.put(f.getTicker(), f));
    }

    @Override
    public Optional<Futures> getFutures(String ticker) {
        return Optional.ofNullable(futuresMap.get(ticker));
    }

    @Override
    public List<Futures> getAllFutures() {
        return futuresMap.values().stream().toList();
    }
}
