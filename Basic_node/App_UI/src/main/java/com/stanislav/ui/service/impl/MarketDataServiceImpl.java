package com.stanislav.ui.service.impl;

import com.stanislav.ui.configuration.AnastasiaUIConfig;
import com.stanislav.ui.configuration.auth.TokenAuthService;
import com.stanislav.ui.exception.NotFoundException;
import com.stanislav.ui.model.ExchangeMarket;
import com.stanislav.ui.model.market.Futures;
import com.stanislav.ui.model.market.Stock;
import com.stanislav.ui.service.MarketDataService;
import com.stanislav.ui.utils.GetRequestParametersBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

@Service("marketDataService")
public class MarketDataServiceImpl implements MarketDataService {

    private static final String resource = AnastasiaUIConfig.BACKEND_RESOURCE + "market/";
    private static final String SORT_BY = "sort-by";
    private static final String SORT_ORDER = "sort-order";

    private final RestTemplate restTemplate;
    private final HttpHeaders authorization;


    @Autowired
    public MarketDataServiceImpl(RestTemplate restTemplate, TokenAuthService authService) {
        this.authorization = authService.authorize();
        this.restTemplate = restTemplate;
    }

    @Caching()
    @Override
    public Stock getStock(ExchangeMarket exchangeMarket, String ticker) {
        String url = resource +
                exchangeMarket +
                "/securities/" +
                "stock/" +
                ticker;
        ResponseEntity<Stock> response = restTemplate
                .exchange(url, HttpMethod.GET, new HttpEntity<>(authorization), Stock.class);
        if (response.hasBody()) {
            return response.getBody();
        } else {
            throw new NotFoundException(
                    "Not found stock by ticker='%s' at the exchange market '%s'".formatted(ticker, exchangeMarket));
        }
    }

    @Cacheable(value = "stock:exchange", keyGenerator = "keyGeneratorById")
    @Override
    public List<Stock> getStocks(ExchangeMarket exchangeMarket) {
        return getStocks(exchangeMarket, SortByColumn.NONE, SortOrder.asc);
    }

    @Cacheable(value = "stock:exchange", keyGenerator = "keyGeneratorById")
    @Override
    public List<Stock> getStocks(ExchangeMarket exchangeMarket, SortByColumn sortByColumn, SortOrder sortOrder) {
        GetRequestParametersBuilder getRequest = new GetRequestParametersBuilder(resource);
        getRequest.appendToUrl(exchangeMarket)
                .appendToUrl("/securities/")
                .appendToUrl("stocks");
        getRequest.add(SORT_BY, sortByColumn)
                .add(SORT_ORDER, sortOrder);
        ParameterizedTypeReference<List<Stock>> responseType = new ParameterizedTypeReference<>() {};
        ResponseEntity<List<Stock>> response = restTemplate
                .exchange(getRequest.build(), HttpMethod.GET, new HttpEntity<>(authorization), responseType);
        if (response.hasBody()) {
            return response.getBody();
        } else {
            return Collections.emptyList();
        }
    }

    @Caching
    @Override
    public Futures getFutures(ExchangeMarket exchangeMarket, String ticker) {
        String url = resource +
                exchangeMarket +
                "/securities/" +
                "futures/" +
                ticker;
        ResponseEntity<Futures> response = restTemplate
                .exchange(url, HttpMethod.GET, new HttpEntity<>(authorization), Futures.class);
        if (response.hasBody()) {
            return response.getBody();
        } else {
            throw new NotFoundException(
                    "Not found futures by ticker='%s' at the exchange market '%s'".formatted(ticker, exchangeMarket));
        }
    }

    @Cacheable(value = "futures:exchange", keyGenerator = "keyGeneratorById")
    @Override
    public List<Futures> getFuturesList(ExchangeMarket exchangeMarket) {
        return getFuturesList(exchangeMarket, SortByColumn.NONE, SortOrder.asc);
    }

    @Cacheable(value = "futures:exchange", keyGenerator = "keyGeneratorById")
    @Override
    public List<Futures> getFuturesList(ExchangeMarket exchangeMarket, SortByColumn sortByColumn, SortOrder sortOrder) {
        GetRequestParametersBuilder getRequest = new GetRequestParametersBuilder(resource);
        getRequest.appendToUrl(exchangeMarket)
                .appendToUrl("/securities/")
                .appendToUrl("futures-list");
        getRequest.add(SORT_BY, sortByColumn)
                .add(SORT_ORDER, sortOrder);
        ParameterizedTypeReference<List<Futures>> responseType = new ParameterizedTypeReference<>() {};
        ResponseEntity<List<Futures>> response = restTemplate
                .exchange(getRequest.build(), HttpMethod.GET, new HttpEntity<>(authorization), responseType);
        if (response.hasBody()) {
            return response.getBody();
        } else {
            return Collections.emptyList();
        }
    }
}
