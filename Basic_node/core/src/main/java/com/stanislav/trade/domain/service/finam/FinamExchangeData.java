/*
 * Stanislav Kuprienko *** Omsk, Russia
 */

package com.stanislav.trade.domain.service.finam;

import com.stanislav.trade.domain.service.ExchangeData;
import com.stanislav.trade.domain.service.securities_dto.FinamSecuritiesResponse;
import com.stanislav.trade.entities.Board;
import com.stanislav.trade.entities.markets.Futures;
import com.stanislav.trade.entities.markets.Stock;
import com.stanislav.trade.entities.user.Account;
import com.stanislav.trade.utils.ApiDataParser;
import com.stanislav.trade.utils.GetQueryBuilder;
import com.stanislav.trade.utils.RestConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;

import java.util.Collections;
import java.util.List;

import static com.stanislav.trade.domain.service.finam.FinamExchangeData.Args.*;
import static com.stanislav.trade.domain.service.finam.FinamExchangeData.Resource.*;

@Service("finamExchangeData")
public class FinamExchangeData implements ExchangeData {

    private final String token;
    private final ApiDataParser dataParser;
    private final RestConsumer restConsumer;


    public FinamExchangeData(@Autowired @Qualifier("jsonParser") ApiDataParser dataParser,
                             @Autowired RestConsumer restConsumer,
                             @Value("${api.finam}") String resource,
                             @Value("${token}") String token) {
        this.dataParser = dataParser;
        this.restConsumer = restConsumer;
        this.restConsumer.setAuthorization(RestConsumer.Authorization.API_KEY);
        this.restConsumer.setResource(resource);
        this.token = token;
    }


    @Override
    public Stock getStock(String ticker) {
        GetQueryBuilder query = new GetQueryBuilder(SECURITIES.value);
        query.add(BOARD.value, Board.TQBR).add(SEC_CODE.value, ticker);
        try {
            String response = restConsumer.doRequest(query.build(), HttpMethod.GET, token);
            String[] layers = {"data", "securities"};
            List<FinamSecuritiesResponse> dtoList = dataParser.parseObjectsList(response, FinamSecuritiesResponse.class, layers);
            if (dtoList.isEmpty()) {
                return Stock.emptyStock();
            } else {
                return dtoList.get(0).toStockClass();
            }
        } catch (HttpStatusCodeException e) {
            e.printStackTrace();
            //TODO
            return Stock.emptyStock();
        }
    }

    @Override
    public List<Stock> getStocks() {
        GetQueryBuilder query = new GetQueryBuilder(SECURITIES.value);
        query.add(BOARD.value, Board.TQBR);
        try {
            String response = restConsumer.doRequest(query.build(), HttpMethod.GET, token);
            String[] layers = {"data", "securities"};
            List<FinamSecuritiesResponse> dtoList = dataParser.parseObjectsList(response, FinamSecuritiesResponse.class, layers);
            if (dtoList.isEmpty()) {
                return Collections.emptyList();
            } else {
                return dtoList.stream().map(FinamSecuritiesResponse::toStockClass).toList();
            }
        } catch (HttpStatusCodeException e) {
            e.printStackTrace();
            //TODO
            return Collections.emptyList();
        }
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


    enum Args {
        BOARD("Board"),
        SEC_CODE("Seccode");

        final String value;
        Args(String value) {
            this.value = value;
        }
    }

    enum Resource {

        SECURITIES("securities");

        final String value;
        Resource(String value) {
            this.value = value;
        }
    }
}