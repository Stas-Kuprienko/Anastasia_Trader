/*
 * Stanislav Kuprienko *** Omsk, Russia
 */

/*
 * Stanislav Kuprienko *** Omsk, Russia
 */

package com.stanislav.trade.web.controller;

import com.stanislav.trade.domain.service.MarketData;
import com.stanislav.trade.entities.markets.Stock;
import com.stanislav.trade.entities.user.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/market")
public final class MarketController {

    @Autowired
    @Qualifier("finamMarketData")
    private MarketData marketData;


    @GetMapping("/stock/{ticker}")
    public Stock getStock(@PathVariable("ticker") String ticker, @RequestParam("id") String id) {

        Account account = null;
        return marketData.getStock(account, ticker);
    }

    @GetMapping("/stocks")
    public List<Stock> getStocks(@RequestParam("id") String id) {

        Account account = null;
        return marketData.getStocks(account);
    }
}