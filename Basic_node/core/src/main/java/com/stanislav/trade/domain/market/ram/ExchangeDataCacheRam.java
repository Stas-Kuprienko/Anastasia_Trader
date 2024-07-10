package com.stanislav.trade.domain.market.ram;

import com.stanislav.trade.domain.market.ExchangeDataCache;
import com.stanislav.trade.entities.markets.Futures;
import com.stanislav.trade.entities.markets.Stock;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Service
@EnableScheduling
public class ExchangeDataCacheRam implements ExchangeDataCache {

    //TODO TEMPORARY SOLUTION, NEEDED TO REMAKE BY REDIS

    private final ConcurrentHashMap<String, Stock> stocksMap;
    private final ConcurrentHashMap<String, Futures> futuresMap;

    private LocalDateTime stocksUpdated;
    private LocalDateTime futuresUpdated;

    public ExchangeDataCacheRam() {
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
        stocksUpdated = LocalDateTime.now();
    }

    @Override
    public Optional<Stock> getStock(String ticker) {
        return Optional.ofNullable(stocksMap.get(ticker));
    }

    @Override
    public List<Stock> getAllStocks() {
        return stocksMap.values().stream().toList();
    }

    @Override
    public void add(Futures futures) {
        futuresMap.put(futures.getTicker(), futures);
    }

    @Override
    public void addFuturesList(List<Futures> futuresList) {
        futuresList.forEach(f -> futuresMap.put(f.getTicker(), f));
        futuresUpdated = LocalDateTime.now();
    }

    @Override
    public Optional<Futures> getFutures(String ticker) {
        return Optional.ofNullable(futuresMap.get(ticker));
    }

    @Override
    public List<Futures> getAllFutures() {
        return futuresMap.values().stream().sorted().toList();
    }

    @Override
    public LocalDateTime getStocksUpdated() {
        return stocksUpdated;
    }

    @Override
    public void stocksUpdated() {
        this.stocksUpdated = LocalDateTime.now();
    }

    @Override
    public LocalDateTime getFuturesUpdated() {
        return futuresUpdated;
    }

    @Override
    public void futuresUpdated() {
        this.futuresUpdated = LocalDateTime.now();
    }

    @Scheduled(timeUnit = TimeUnit.HOURS, fixedDelay = 24, initialDelay = 24)
    public void clearCache() {
        stocksMap.clear();
        futuresMap.clear();
    }
}