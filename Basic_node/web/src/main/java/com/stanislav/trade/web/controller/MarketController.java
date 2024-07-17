/*
 * Stanislav Kuprienko *** Omsk, Russia
 */

package com.stanislav.trade.web.controller;

import com.stanislav.trade.domain.market.ExchangeData;
import com.stanislav.trade.entities.markets.Futures;
import com.stanislav.trade.entities.markets.Stock;
import com.stanislav.trade.web.controller.service.ErrorController;
import com.stanislav.trade.web.controller.service.ErrorCase;
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

    public static final String STOCK_URI = "stock";
    public static final String FUTURES_URI = "futures";

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
            return ErrorController.FORWARD_ERROR + ErrorCase.NOT_FOUND;
        }
        model.addAttribute(SEC_ITEM, stock.get());
        model.addAttribute("type", STOCK_URI);
        return ITEM_PAGE;
    }

    @GetMapping("/stocks")
    public String getStocks(Model model,
                            @RequestParam(value = "sort-by", required = false) String sortByParam,
                            @RequestParam(value = "sort-order", required = false) String sortOrderParam) {
        List<Stock> stocks;
        if (sortByParam != null) {
            ExchangeData.SortByColumn sortByColumn;
            ExchangeData.SortOrder sortOrder;
            try {
                sortByColumn = ExchangeData.SortByColumn.valueOf(sortByParam);
                sortOrder = ExchangeData.SortOrder.valueOf(sortOrderParam);
            } catch (IllegalArgumentException e) {
                log.warn(e.getMessage());
                sortByColumn = ExchangeData.SortByColumn.NONE;
                sortOrder = ExchangeData.SortOrder.asc;
            }
            stocks = exchangeData.getStocks(sortByColumn, sortOrder);
        } else {
            stocks = exchangeData.getStocks();
        }
        model.addAttribute(SEC_LIST, stocks);
        model.addAttribute("type", STOCK_URI);
        return LIST_PAGE;
    }

    @GetMapping("/futures/{ticker}")
    public String getFutures(@PathVariable("ticker") String ticker, Model model) {
        Optional<Futures> futures = exchangeData.getFutures(ticker);
        if (futures.isEmpty()) {
            log.info(ticker + " is not found");
            return ErrorController.FORWARD_ERROR + ErrorCase.NOT_FOUND;
        }
        model.addAttribute(SEC_ITEM, futures.get());
        model.addAttribute("type", FUTURES_URI);
        return ITEM_PAGE;
    }

    @GetMapping("/futures")
    public String getFuturesList(Model model,
                                 @RequestParam(value = "sort-by", required = false) String sortByParam,
                                 @RequestParam(value = "sort-order", required = false) String sortOrderParam) {
        List<Futures> futuresList;
        if (sortByParam != null) {
            ExchangeData.SortByColumn sortByColumn;
            ExchangeData.SortOrder sortOrder;
            try {
                sortByColumn = ExchangeData.SortByColumn.valueOf(sortByParam);
                sortOrder = ExchangeData.SortOrder.valueOf(sortOrderParam);
            } catch (IllegalArgumentException e) {
                log.warn(e.getMessage());
                sortByColumn = ExchangeData.SortByColumn.NONE;
                sortOrder = ExchangeData.SortOrder.asc;
            }
            futuresList = exchangeData.getFuturesList(sortByColumn, sortOrder);
        } else {
            futuresList = exchangeData.getFuturesList();
        }
        model.addAttribute(SEC_LIST, futuresList);
        model.addAttribute("type", FUTURES_URI);
        return LIST_PAGE;
    }
}