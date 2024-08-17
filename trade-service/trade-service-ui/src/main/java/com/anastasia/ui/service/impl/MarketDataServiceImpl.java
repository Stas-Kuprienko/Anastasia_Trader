package com.anastasia.ui.service.impl;

import com.anastasia.ui.configuration.AnastasiaUIConfig;
import com.anastasia.ui.exception.NotFoundException;
import com.anastasia.ui.model.ExchangeMarket;
import com.anastasia.ui.model.market.Futures;
import com.anastasia.ui.model.market.Securities;
import com.anastasia.ui.model.market.Stock;
import com.anastasia.ui.model.user.User;
import com.anastasia.ui.service.DataCacheService;
import com.anastasia.ui.service.MarketDataService;
import com.anastasia.ui.service.UserService;
import com.anastasia.ui.utils.GetRequestParametersBuilder;
import com.anastasia.ui.utils.MyRestClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Service("marketDataService")
public class MarketDataServiceImpl implements MarketDataService {

    private static final String resource = AnastasiaUIConfig.BACKEND_RESOURCE + "market/";
    private static final String SORT_BY = "sort-by";
    private static final String SORT_ORDER = "sort-order";

    private final DataCacheService dataCacheService;
    private final MyRestClient myRestClient;
    private final UserService userService;


    @Autowired
    public MarketDataServiceImpl(DataCacheService dataCacheService, MyRestClient myRestClient, UserService userService) {
        this.dataCacheService = dataCacheService;
        this.myRestClient = myRestClient;
        this.userService = userService;
    }

    @Override
    public Stock getStock(long userId, ExchangeMarket exchangeMarket, String ticker) {
        String key = exchangeMarket.toString() + ':' + Stock.class.getSimpleName();
        Stock stock = dataCacheService.getAndParseFromJson(key, ticker, Stock.class);
        if (stock == null) {
            String url = resource +
                    exchangeMarket +
                    "/securities/" +
                    "stock/" +
                    ticker;
            User user = userService.findById(userId);
            ResponseEntity<Stock> response = myRestClient.get(user, url, Stock.class);
            if (response.hasBody() && response.getBody() != null) {
                stock = response.getBody();
            } else {
                throw new NotFoundException(
                        "Not found stock by ticker='%s' at the exchange market '%s'".formatted(ticker, exchangeMarket));
            }
        }
        return stock;
    }

    @Override
    public List<Stock> getStocks(long userId, ExchangeMarket exchangeMarket) {
        return getStocks(userId, exchangeMarket, SortByColumn.NONE, SortOrder.asc);
    }

    @Override
    public List<Stock> getStocks(long userId, ExchangeMarket exchangeMarket, SortByColumn sortByColumn, SortOrder sortOrder) {
        String key = exchangeMarket.toString() + ':' + Stock.class.getSimpleName();
        List<Stock> stocks = dataCacheService.getAndParseListForKeyFromJson(key, Stock.class);
        if (stocks == null || stocks.isEmpty()) {
            GetRequestParametersBuilder getRequest = new GetRequestParametersBuilder(resource);

            getRequest.appendToUrl(exchangeMarket)
                    .appendToUrl("/securities/")
                    .appendToUrl("stocks");
            getRequest.add(SORT_BY, sortByColumn)
                    .add(SORT_ORDER, sortOrder);

            User user = userService.findById(userId);
            ParameterizedTypeReference<List<Stock>> responseType = new ParameterizedTypeReference<>() {};

            ResponseEntity<? extends Collection<Stock>> response = myRestClient.get(user, getRequest.build(), responseType);

            if (response.hasBody() && response.getBody() != null) {
                stocks = (List<Stock>) response.getBody();
                putSecuritiesToCache(key, stocks);
            } else {
                return Collections.emptyList();
            }
        }
        //TODO check elements quantity
        if (sortByColumn == SortByColumn.TRADE_VOLUME) {
            return stocks.stream().sorted().toList();
        }
        return stocks;
    }

    @Override
    public Futures getFutures(long userId, ExchangeMarket exchangeMarket, String ticker) {
        String key = exchangeMarket.toString() + ':' + Futures.class.getSimpleName();
        Futures futures = dataCacheService.getAndParseFromJson(key, ticker, Futures.class);
        if (futures == null) {
            String url = resource +
                    exchangeMarket +
                    "/securities/" +
                    "futures/" +
                    ticker;
            User user = userService.findById(userId);
            ResponseEntity<Futures> response = myRestClient.get(user, url, Futures.class);
            if (response.hasBody() && response.getBody() != null) {
                futures = response.getBody();
            } else {
                throw new NotFoundException(
                        "Not found futures by ticker='%s' at the exchange market '%s'".formatted(ticker, exchangeMarket));
            }
        }
        return futures;
    }

    @Override
    public List<Futures> getFuturesList(long userId, ExchangeMarket exchangeMarket) {
        return getFuturesList(userId, exchangeMarket, SortByColumn.NONE, SortOrder.asc);
    }

    @Override
    public List<Futures> getFuturesList(long userId, ExchangeMarket exchangeMarket, SortByColumn sortByColumn, SortOrder sortOrder) {
        String key = exchangeMarket.toString() + ':' + Futures.class.getSimpleName();
        List<Futures> futuresList = dataCacheService.getAndParseListForKeyFromJson(key, Futures.class);
        if (futuresList == null || futuresList.isEmpty()) {
            GetRequestParametersBuilder getRequest = new GetRequestParametersBuilder(resource);

            getRequest.appendToUrl(exchangeMarket)
                    .appendToUrl("/securities/")
                    .appendToUrl("futures-list");
            getRequest.add(SORT_BY, sortByColumn)
                    .add(SORT_ORDER, sortOrder);

            User user = userService.findById(userId);
            ParameterizedTypeReference<List<Futures>> responseType = new ParameterizedTypeReference<>() {};

            ResponseEntity<? extends Collection<Futures>> response = myRestClient.get(user, getRequest.build(), responseType);

            if (response.hasBody() && response.getBody() != null) {
                futuresList = (List<Futures>) response.getBody();
                putSecuritiesToCache(key, futuresList);
            } else {
                return Collections.emptyList();
            }
        }
        //TODO check elements quantity
        if (sortByColumn == SortByColumn.TRADE_VOLUME) {
            return futuresList.stream().sorted().toList();
        }
        return futuresList;
    }


    private void putSecuritiesToCache(String key, List<? extends Securities> securitiesList) {
        for(Securities s : securitiesList) {
            dataCacheService.putAsJson(key, s.getTicker(), s);
        }
    }
}