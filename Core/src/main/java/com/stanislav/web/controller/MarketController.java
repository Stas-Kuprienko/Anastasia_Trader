package com.stanislav.web.controller;

import com.stanislav.entities.candles.DayCandles;
import com.stanislav.entities.candles.IntraDayCandles;
import com.stanislav.entities.markets.Stock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import com.stanislav.database.DatabaseRepository;
import com.stanislav.entities.user.Account;
import com.stanislav.domain.trading.MarketData;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/market")
public final class MarketController {

    @Autowired
    @Qualifier("finamMarketData")
    private MarketData marketData;

    @Autowired
    private DatabaseRepository databaseRepository;


    @GetMapping(value = "/stock/{ticker}")
    public Stock getStock(@PathVariable String ticker, @RequestParam String id) {

        Account account = databaseRepository.accountPersistence().getById(id);
        return marketData.getStock(account, ticker);
    }

    @GetMapping(value = "/stocks/{ticker}")
    public List<Stock> getStocks(@RequestParam String id) {

        Account account = databaseRepository.accountPersistence().getById(id);
        return marketData.getStocks(account);
    }

    @GetMapping(value = "/day-candles/{ticker}")
    public DayCandles getDayCandles(@PathVariable String ticker, @RequestParam String id, @RequestParam String timeFrame,
                                    @RequestParam LocalDate from, @RequestParam LocalDate to, @RequestParam(required = false) Integer count) {

        Account account = databaseRepository.accountPersistence().getById(id);
        return marketData.getDayCandles(account, ticker, timeFrame, from, to, count);
    }

    @GetMapping(value = "/intra-day-candles/{ticker}")
    public IntraDayCandles getIntraDayCandles(@PathVariable String ticker, @RequestParam String id, @RequestParam String timeFrame,
                                              @RequestParam LocalDateTime from, @RequestParam LocalDateTime to, @RequestParam(required = false) Integer count) {

        Account account = databaseRepository.accountPersistence().getById(id);
        return marketData.getIntraDayCandles(account, ticker, timeFrame, from, to, count);
    }
}