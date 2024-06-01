/*
 * Stanislav Kuprienko *** Omsk, Russia
 */

/*
 * Stanislav Kuprienko *** Omsk, Russia
 */

package com.stanislav.trade.web.controller;

import com.stanislav.trade.domain.service.ExchangeData;
import com.stanislav.trade.entities.markets.Stock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/market")
public final class MarketController {

    @Autowired
    @Qualifier("moexExchangeData")
    private ExchangeData exchangeData;


    @GetMapping("/stock/{ticker}")
    public Stock getStock(@PathVariable("ticker") String ticker, @RequestParam("id") String id) {
        return exchangeData.getStock(ticker).orElseThrow();
    }

    @GetMapping("/stocks")
    public List<Stock> getStocks(@RequestParam("id") String id) {
        return exchangeData.getStocks();
    }
}