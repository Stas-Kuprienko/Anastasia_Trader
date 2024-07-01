/*
 * Stanislav Kuprienko *** Omsk, Russia
 */

/*
 * Stanislav Kuprienko *** Omsk, Russia
 */

package com.stanislav.trade.web.controller;

import com.stanislav.trade.domain.service.ExchangeData;
import com.stanislav.trade.entities.markets.Stock;
import com.stanislav.trade.web.controller.service.ErrorController;
import com.stanislav.trade.web.service.ErrorCase;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Slf4j
@Controller
@RequestMapping("/market")
public final class MarketController {

    private static final String STOCK_URI = "stock";
    private static final String FUTURES_URI = "futures";

    private static final String LIST_PAGE = "securities";
    private static final String ITEM_PAGE = "security_item";
    private static final String SEC_LIST = "secList";
    private static final String SEC_ITEM = "secItem";

    private final ExchangeData exchangeData;


    @Autowired
    public MarketController(@Qualifier("moexExchangeData") ExchangeData exchangeData) {
        this.exchangeData = exchangeData;
    }


    @GetMapping("/stock/{ticker}")
    public String getStock(@PathVariable("ticker") String ticker, Model model) {
        Optional<Stock> stock = exchangeData.getStock(ticker);
        if (stock.isEmpty()) {
            log.info(ticker + " is not found");
            return ErrorController.URL + ErrorCase.NOT_FOUND;
        }
        model.addAttribute(SEC_ITEM, stock.get());
        model.addAttribute("type", STOCK_URI);
        return ITEM_PAGE;
    }

    @GetMapping("/stocks")
    public String getStocks(Model model, HttpServletRequest request) {
        List<Stock> stocks = exchangeData.getStocks(ExchangeData.SortByColumn.TRADE_VOLUME, ExchangeData.SortOrder.desc);
        model.addAttribute(SEC_LIST, stocks);
        model.addAttribute("type", STOCK_URI);
        return LIST_PAGE;
    }


}