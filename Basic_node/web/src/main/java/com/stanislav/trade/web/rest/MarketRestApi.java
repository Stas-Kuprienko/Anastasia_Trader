package com.stanislav.trade.web.rest;

import com.stanislav.trade.domain.market.ExchangeData;
import com.stanislav.trade.entities.markets.Futures;
import com.stanislav.trade.entities.markets.Stock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/market")
public class MarketRestApi {

    @Autowired
    @Qualifier("moexExchangeData")
    private ExchangeData exchangeData;


    @GetMapping("/stock/{ticker}")
    public Stock getStock(@PathVariable("ticker") String ticker) {
        return exchangeData.getStock(ticker).orElseThrow();
    }

    @GetMapping("/stocks")
    public List<Stock> getStocks() {
        return exchangeData.getStocks(ExchangeData.SortByColumn.TRADE_VOLUME, ExchangeData.SortOrder.desc);
    }

    @GetMapping("/futures")
    public Futures getFutures(@PathVariable("ticker") String ticker) {
        return exchangeData.getFutures(ticker).orElseThrow();
    }
}
