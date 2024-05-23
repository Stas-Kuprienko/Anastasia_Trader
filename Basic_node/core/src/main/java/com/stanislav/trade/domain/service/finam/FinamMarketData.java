/*
 * Stanislav Kuprienko *** Omsk, Russia
 */

package com.stanislav.trade.domain.service.finam;

import com.stanislav.trade.domain.service.MarketData;
import com.stanislav.trade.domain.service.securities_dto.FinamSecuritiesResponse;
import com.stanislav.trade.entities.Board;
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

import static com.stanislav.trade.domain.service.finam.FinamMarketData.Args.*;
import static com.stanislav.trade.domain.service.finam.FinamMarketData.Resource.*;

@Service("finamMarketData")
public class FinamMarketData implements MarketData {

    private final ApiDataParser dataParser;
    private final RestConsumer restConsumer;


    public FinamMarketData(@Autowired @Qualifier("jsonParser") ApiDataParser dataParser,
                           @Autowired RestConsumer restConsumer,
                           @Value("${api.finam}") String resource) {
        this.dataParser = dataParser;
        this.restConsumer = restConsumer;
        this.restConsumer.setAuthorization(RestConsumer.Authorization.API_KEY);
        this.restConsumer.setResource(resource);
    }


    @Override
    public Stock getStock(Account account, String ticker) {
        GetQueryBuilder query = new GetQueryBuilder(SECURITIES.value);
        query.add(BOARD.value, Board.TQBR).add(SEC_CODE.value, ticker);
        try {
            String response = restConsumer.doRequest(query.build(), HttpMethod.GET, account.getToken());
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
    public List<Stock> getStocks(Account account) {
        GetQueryBuilder query = new GetQueryBuilder(SECURITIES.value);
        query.add(BOARD.value, Board.TQBR);
        try {
            String response = restConsumer.doRequest(query.build(), HttpMethod.GET, account.getToken());
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