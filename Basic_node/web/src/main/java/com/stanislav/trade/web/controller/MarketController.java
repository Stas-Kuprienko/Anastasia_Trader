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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/market")
public final class MarketController {

    @Autowired
    @Qualifier("moexExchangeData")
    private ExchangeData exchangeData;


    @GetMapping("/stock/{ticker}")
    public String getStock(@PathVariable("ticker") String ticker, Model model) {

        Stock stock = exchangeData.getStock(ticker).orElseThrow();
        model.addAttribute("stock", stock);

        //TODO
        return "market page";
    }

    @GetMapping("/stocks")
    public String getStocks(Model model) {
        List<Stock> stocks = exchangeData.getStocks(ExchangeData.SortByColumn.TRADE_VOLUME, ExchangeData.SortOrder.desc);
        model.addAttribute("stocks", stocks);

        //TODO
        return "market page";
    }
}