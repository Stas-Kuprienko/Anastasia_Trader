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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/market")
public final class MarketController {

    @Autowired
    @Qualifier("moexExchangeData")
    private ExchangeData exchangeData;


    @GetMapping("/stock/{ticker}")
    public Stock getStock(@AuthenticationPrincipal UserDetails userDetails,
                          @PathVariable("ticker") String ticker) {

        return exchangeData.getStock(ticker).orElseThrow();
    }

    @GetMapping("/stocks")
    public List<Stock> getStocks() {
        return exchangeData.getStocks(ExchangeData.SortByColumn.TRADE_VOLUME, ExchangeData.SortOrder.desc);
    }
}