/*
 * Stanislav Kuprienko *** Omsk, Russia
 */

/*
 * Stanislav Kuprienko *** Omsk, Russia
 */

package com.stanislav.trade.web.controller;

import com.stanislav.trade.datasource.AccountDao;
import com.stanislav.trade.datasource.UserDao;
import com.stanislav.trade.domain.trading.MarketData;
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

    @Autowired
    private AccountDao accountDao;

    @Autowired
    private UserDao userDao;


    @GetMapping("/stock/{ticker}")
    public Stock getStock(@PathVariable("ticker") String ticker, @RequestParam("id") String id) {

        Account account = accountDao.findById(0L).get();
        return marketData.getStock(account, ticker);
    }

    @GetMapping("/stocks")
    public List<Stock> getStocks(@RequestParam("id") String id) {

        Account account = accountDao.findById(0L).get();
        return marketData.getStocks(account);
    }
}