/*
 * Stanislav Kuprienko *** Omsk, Russia
 */

package com.stanislav.ui.controller.trade;

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


    @GetMapping("/stock/{ticker}")
    public String getStock(@PathVariable("ticker") String ticker, Model model) {
        Stock stock = marketDataService.getStock(ticker);
        model.addAttribute(SEC_ITEM, stock);
        model.addAttribute("type", STOCK_URI);
        return ITEM_PAGE;
    }

    @GetMapping("/stocks")
    public String getStocks(Model model,
                            @RequestParam(value = "sort-by", required = false) String sortByParam,
                            @RequestParam(value = "sort-order", required = false) String sortOrderParam) {
        List<Stock> stocks;
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
            stocks = marketDataService.getStocks(sortByColumn, sortOrder);
        } else {
            stocks = marketDataService.getStocks();
        }
        model.addAttribute(SEC_LIST, stocks);
        model.addAttribute("type", STOCK_URI);
        return LIST_PAGE;
    }

    @GetMapping("/futures/{ticker}")
    public String getFutures(@PathVariable("ticker") String ticker, Model model) {
        Futures futures = marketDataService.getFutures(ticker);
        model.addAttribute(SEC_ITEM, futures);
        model.addAttribute("type", FUTURES_URI);
        return ITEM_PAGE;
    }

    @GetMapping("/futures")
    public String getFuturesList(Model model,
                                 @RequestParam(value = "sort-by", required = false) String sortByParam,
                                 @RequestParam(value = "sort-order", required = false) String sortOrderParam) {
        List<Futures> futuresList;
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
            futuresList = marketDataService.getFuturesList(sortByColumn, sortOrder);
        } else {
            futuresList = marketDataService.getFuturesList();
        }
        model.addAttribute(SEC_LIST, futuresList);
        model.addAttribute("type", FUTURES_URI);
        return LIST_PAGE;
    }
}