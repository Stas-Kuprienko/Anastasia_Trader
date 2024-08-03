/*
 * Stanislav Kuprienko *** Omsk, Russia
 */

package com.stanislav.ui.controller.trade;

import com.stanislav.ui.model.ExchangeMarket;
import com.stanislav.ui.model.market.Futures;
import com.stanislav.ui.model.market.Stock;
import com.stanislav.ui.service.MarketDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;

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

    private final MarketDataService marketDataService;


    @Autowired
    public MarketController(MarketDataService marketDataService) {
        this.marketDataService = marketDataService;
    }


    @GetMapping("/{exchange}/stock/{ticker}")
    public String getStock(@PathVariable("ticker") String ticker,
                           @PathVariable("exchange") String exchange,
                           Model model) {
        ExchangeMarket exchangeMarket = ExchangeMarket.valueOf(exchange);
        Stock stock = marketDataService.getStock(exchangeMarket, ticker);
        model.addAttribute(SEC_ITEM, stock);
        model.addAttribute("type", STOCK_URI);
        return ITEM_PAGE;
    }

    @GetMapping("/{exchange}/stocks")
    public String getStocks(@PathVariable("exchange") String exchange,
                            @RequestParam(value = "sort-by", required = false) String sortByParam,
                            @RequestParam(value = "sort-order", required = false) String sortOrderParam,
                            Model model) {
        List<Stock> stocks;
        ExchangeMarket exchangeMarket = ExchangeMarket.valueOf(exchange);
        if (sortByParam != null) {
            MarketDataService.SortByColumn sortByColumn;
            MarketDataService.SortOrder sortOrder;
            try {
                sortByColumn = MarketDataService.SortByColumn.valueOf(sortByParam);
                sortOrder = MarketDataService.SortOrder.valueOf(sortOrderParam);
            } catch (IllegalArgumentException e) {
                log.warn(e.getMessage());
                sortByColumn = MarketDataService.SortByColumn.NONE;
                sortOrder = MarketDataService.SortOrder.asc;
            }
            stocks = marketDataService.getStocks(exchangeMarket, sortByColumn, sortOrder);
        } else {
            stocks = marketDataService.getStocks(exchangeMarket);
        }
        model.addAttribute(SEC_LIST, stocks);
        model.addAttribute("type", STOCK_URI);
        return LIST_PAGE;
    }

    @GetMapping("/{exchange}/futures/{ticker}")
    public String getFutures(@PathVariable("exchange") String exchange,
                             @PathVariable("ticker") String ticker,
                             Model model) {
        ExchangeMarket exchangeMarket = ExchangeMarket.valueOf(exchange);
        Futures futures = marketDataService.getFutures(exchangeMarket, ticker);
        model.addAttribute(SEC_ITEM, futures);
        model.addAttribute("type", FUTURES_URI);
        return ITEM_PAGE;
    }

    @GetMapping("/{exchange}/futures")
    public String getFuturesList(@PathVariable("exchange") String exchange,
                                 @RequestParam(value = "sort-by", required = false) String sortByParam,
                                 @RequestParam(value = "sort-order", required = false) String sortOrderParam,
                                 Model model) {
        List<Futures> futuresList;
        ExchangeMarket exchangeMarket = ExchangeMarket.valueOf(exchange);
        if (sortByParam != null) {
            MarketDataService.SortByColumn sortByColumn;
            MarketDataService.SortOrder sortOrder;
            try {
                sortByColumn = MarketDataService.SortByColumn.valueOf(sortByParam);
                sortOrder = MarketDataService.SortOrder.valueOf(sortOrderParam);
            } catch (IllegalArgumentException e) {
                log.warn(e.getMessage());
                sortByColumn = MarketDataService.SortByColumn.NONE;
                sortOrder = MarketDataService.SortOrder.asc;
            }
            futuresList = marketDataService.getFuturesList(exchangeMarket, sortByColumn, sortOrder);
        } else {
            futuresList = marketDataService.getFuturesList(exchangeMarket);
        }
        model.addAttribute(SEC_LIST, futuresList);
        model.addAttribute("type", FUTURES_URI);
        return LIST_PAGE;
    }
}