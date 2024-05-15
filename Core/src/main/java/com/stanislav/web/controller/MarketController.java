package com.stanislav.web.controller;

import com.stanislav.datasource.AccountPersistence;
import com.stanislav.datasource.UserPersistence;
import com.stanislav.domain.trading.MarketData;
import com.stanislav.entities.markets.Stock;
import com.stanislav.entities.user.Account;
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

    @Autowired
    private AccountPersistence accountPersistence;

    @Autowired
    private UserPersistence userPersistence;


    @GetMapping("/stock/{ticker}")
    public Stock getStock(@PathVariable("ticker") String ticker, @RequestParam("id") String id) {

        Account account = accountPersistence.getById(id);
        return marketData.getStock(account, ticker);
    }

    @GetMapping("/stocks")
    public List<Stock> getStocks(@RequestParam("id") String id) {

        Account account = accountPersistence.getById(id);
        return marketData.getStocks(account);
    }
}