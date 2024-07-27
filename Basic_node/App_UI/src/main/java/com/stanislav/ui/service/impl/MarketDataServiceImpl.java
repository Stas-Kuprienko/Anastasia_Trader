package com.stanislav.ui.service.impl;

import com.stanislav.ui.model.market.Futures;
import com.stanislav.ui.model.market.Stock;
import com.stanislav.ui.service.MarketDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service("marketDataService")
public class MarketDataServiceImpl implements MarketDataService {

    private final RestTemplate restTemplate;

    @Autowired
    public MarketDataServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }


    @Override
    public Stock getStock(String ticker) {
        return null;
    }

    @Override
    public List<Stock> getStocks() {
        return null;
    }

    @Override
    public List<Stock> getStocks(SortByColumn sortByColumn, SortOrder sortOrder) {
        return null;
    }

    @Override
    public Futures getFutures(String ticker) {
        return null;
    }

    @Override
    public List<Futures> getFuturesList() {
        return null;
    }

    @Override
    public List<Futures> getFuturesList(SortByColumn sortByColumn, SortOrder sortOrder) {
        return null;
    }
}
