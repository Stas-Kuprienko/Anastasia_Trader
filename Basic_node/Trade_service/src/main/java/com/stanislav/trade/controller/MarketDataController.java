package com.stanislav.trade.controller;

import com.stanislav.trade.domain.market.ExchangeDataProvider;
import com.stanislav.trade.entities.ExchangeMarket;
import com.stanislav.trade.entities.markets.Futures;
import com.stanislav.trade.entities.markets.Stock;
import com.stanislav.trade.exception.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/market")
public class MarketDataController {

    private final HashMap<ExchangeMarket, ExchangeDataProvider> exchangeDataMap;

    @Autowired
    public MarketDataController(List<ExchangeDataProvider> exchangeDataProviderList) {
        exchangeDataMap = new HashMap<>();
        for (var e : exchangeDataProviderList) {
            exchangeDataMap.put(e.getExchange(), e);
        }
    }


    @GetMapping("/{exchange}/securities/stock/{ticker}")
    public ResponseEntity<Stock> getStock(@PathVariable("exchange") String exchange,
                                          @PathVariable("ticker") String ticker) {
        ExchangeMarket exchangeMarket = ExchangeMarket.valueOf(exchange);
        ExchangeDataProvider exchangeDataProvider = exchangeDataMap.get(exchangeMarket);
        Stock stock = exchangeDataProvider.getStock(ticker).orElseThrow(() -> new NotFoundException(ticker));
        return ResponseEntity.ok(stock);
    }

    @GetMapping("/{exchange}/securities/stocks")
    public List<Stock> getStocks(@PathVariable("exchange") String exchange,
                                 @RequestParam(value = "sort-by", required = false) String sortByParam,
                                 @RequestParam(value = "sort-order", required = false) String sortOrderParam) {
        ExchangeMarket exchangeMarket = ExchangeMarket.valueOf(exchange);
        ExchangeDataProvider exchangeDataProvider = exchangeDataMap.get(exchangeMarket);
        List<Stock> stocks;
        if (sortByParam != null) {
            ExchangeDataProvider.SortByColumn sortByColumn;
            ExchangeDataProvider.SortOrder sortOrder;
            try {
                sortByColumn = ExchangeDataProvider.SortByColumn.valueOf(sortByParam);
                sortOrder = ExchangeDataProvider.SortOrder.valueOf(sortOrderParam);
            } catch (IllegalArgumentException e) {
                log.warn(e.getMessage());
                sortByColumn = ExchangeDataProvider.SortByColumn.NONE;
                sortOrder = ExchangeDataProvider.SortOrder.asc;
            }
            stocks = exchangeDataProvider.getStocks(sortByColumn, sortOrder);
        } else {
            stocks = exchangeDataProvider.getStocks();
        }
        return stocks;
    }

    @GetMapping("/{exchange}/securities/futures/{ticker}")
    public ResponseEntity<Futures> getFutures(@PathVariable("exchange") String exchange,
                                              @PathVariable("ticker") String ticker) {
        ExchangeMarket exchangeMarket = ExchangeMarket.valueOf(exchange);
        ExchangeDataProvider exchangeDataProvider = exchangeDataMap.get(exchangeMarket);
        Futures futures = exchangeDataProvider.getFutures(ticker).orElseThrow(() -> new NotFoundException(ticker));
        return ResponseEntity.ok(futures);
    }

    @GetMapping("/{exchange}/securities/futures-list")
    public List<Futures> getFuturesList(@PathVariable("exchange") String exchange,
                                        @RequestParam(value = "sort-by", required = false) String sortByParam,
                                        @RequestParam(value = "sort-order", required = false) String sortOrderParam) {
        ExchangeMarket exchangeMarket = ExchangeMarket.valueOf(exchange);
        ExchangeDataProvider exchangeDataProvider = exchangeDataMap.get(exchangeMarket);
        List<Futures> futuresList;
        if (sortByParam != null) {
            ExchangeDataProvider.SortByColumn sortByColumn;
            ExchangeDataProvider.SortOrder sortOrder;
            try {
                sortByColumn = ExchangeDataProvider.SortByColumn.valueOf(sortByParam);
                sortOrder = ExchangeDataProvider.SortOrder.valueOf(sortOrderParam);
            } catch (IllegalArgumentException e) {
                log.warn(e.getMessage());
                sortByColumn = ExchangeDataProvider.SortByColumn.NONE;
                sortOrder = ExchangeDataProvider.SortOrder.asc;
            }
            futuresList = exchangeDataProvider.getFuturesList(sortByColumn, sortOrder);
        } else {
            futuresList = exchangeDataProvider.getFuturesList();
        }
        return futuresList;
    }
}
