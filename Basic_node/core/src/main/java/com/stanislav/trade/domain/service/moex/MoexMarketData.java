package com.stanislav.trade.domain.service.moex;

import com.stanislav.trade.domain.service.MarketData;
import com.stanislav.trade.entities.markets.Stock;
import com.stanislav.trade.entities.user.Account;
import com.stanislav.trade.utils.ApiDataParser;
import com.stanislav.trade.utils.RestConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("moexMarketData")
public class MoexMarketData implements MarketData {

    private final ApiDataParser dataParser;
    private final RestConsumer restConsumer;


    public MoexMarketData(@Autowired @Qualifier("jsonParser") ApiDataParser dataParser,
                          @Autowired RestConsumer restConsumer,
                          @Value("${api.moex}") String resource) {
        this.dataParser = dataParser;
        this.restConsumer = restConsumer;
        this.restConsumer.setResource(resource);
    }


    @Override
    public Stock getStock(Account account, String ticker) {
        return null;
    }

    @Override
    public List<Stock> getStocks(Account account) {
        return null;
    }


    enum Resource {
        SECURITIES("securities"),
        ;

        final String value;
        Resource(String value) {
            this.value = value;
        }
    }
}