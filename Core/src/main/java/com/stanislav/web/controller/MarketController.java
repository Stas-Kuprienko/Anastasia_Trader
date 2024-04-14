package com.stanislav.web.controller;

import com.stanislav.database.AccountPersistence;
import com.stanislav.domain.trading.MarketData;
import com.stanislav.entities.candles.DayCandles;
import com.stanislav.entities.candles.IntraDayCandles;
import com.stanislav.entities.markets.Stock;
import com.stanislav.entities.user.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.List;

@RestController
@RequestMapping("/market")
public final class MarketController {

    @Autowired
    @Qualifier("finamMarketData")
    private MarketData marketData;

    @Autowired
    private AccountPersistence accountPersistence;


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

    @GetMapping("/day-candles/{ticker}")
    public DayCandles getDayCandles(@PathVariable("ticker") String ticker,
                                    @RequestParam("id") String id,
                                    @RequestParam("timeFrame") String timeFrame,
                                    @RequestParam("from") LocalDate from,
                                    @RequestParam("to") LocalDate to,
                                    @RequestParam(value = "count", required = false) Integer count) {

        Account account = accountPersistence.getById(id);
        return marketData.getDayCandles(account, ticker, timeFrame, from, to, count);
    }

    @GetMapping("/intra-day-candles/{ticker}")
    public IntraDayCandles getIntraDayCandles(@PathVariable("ticker") String ticker,
                                              @RequestParam("id") String id,
                                              @RequestParam("timeFrame") String timeFrame,
                                              @RequestParam("from") String from,
                                              @RequestParam("to") String to,
                                              @RequestParam(value = "count", required = false) Integer count) {

        Account account = accountPersistence.getById(id);
        DateTimeFormatter formatter = new DateTimeFormatterBuilder().append(DateTimeFormatter.ISO_DATE_TIME).toFormatter();
        LocalDateTime timeFrom = formatter.parse(from, LocalDateTime::from);
        LocalDateTime timeTo = formatter.parse(to, LocalDateTime::from);
        return marketData.getIntraDayCandles(account, ticker, timeFrame, timeFrom, timeTo, count);
    }
}