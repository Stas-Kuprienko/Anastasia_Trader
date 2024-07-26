/*
 * Stanislav Kuprienko *** Omsk, Russia
 */

package com.stanislav.trade.domain.market.finam;

import com.stanislav.trade.domain.market.ExchangeDataProvider;
import com.stanislav.trade.domain.market.finam.dto.FinamSecuritiesResponseDto;
import com.stanislav.trade.entities.Board;
import com.stanislav.trade.entities.ExchangeMarket;
import com.stanislav.trade.entities.markets.Futures;
import com.stanislav.trade.entities.markets.Stock;
import com.stanislav.trade.utils.ApiDataParser;
import com.stanislav.trade.utils.GetQueryBuilder;
import com.stanislav.trade.utils.RestConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.HttpStatusCodeException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.stanislav.trade.domain.market.finam.FinamExchangeDataProvider.Args.*;
import static com.stanislav.trade.domain.market.finam.FinamExchangeDataProvider.Resource.*;

//@Service("finamExchangeData")
public class FinamExchangeDataProvider implements ExchangeDataProvider {

    private final String token;
    private final ApiDataParser dataParser;
    private final RestConsumer restConsumer;


    public FinamExchangeDataProvider(@Autowired @Qualifier("jsonParser") ApiDataParser dataParser,
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
    public ExchangeMarket getExchange() {
        return ExchangeMarket.MOEX;
    }

    @Override
    public Optional<Stock> getStock(String ticker) {
        GetQueryBuilder query = new GetQueryBuilder(SECURITIES.value);
        query.add(BOARD.value, Board.TQBR).add(SEC_CODE.value, ticker);
        try {
            String response = restConsumer.doRequest(query.build(), HttpMethod.GET, token);
            String[] layers = {"data", "securities"};
            List<FinamSecuritiesResponseDto> dtoList = dataParser.parseObjectsList(response, FinamSecuritiesResponseDto.class, layers);
            if (dtoList.isEmpty()) {
                return Optional.empty();
            } else {
                return Optional.of(dtoList.getFirst().toStockClass());
            }
        } catch (HttpStatusCodeException e) {
            e.printStackTrace();
            //TODO
            return Optional.empty();
        }
    }

    @Override
    public List<Stock> getStocks() {
        GetQueryBuilder query = new GetQueryBuilder(SECURITIES.value);
        query.add(BOARD.value, Board.TQBR);
        try {
            String response = restConsumer.doRequest(query.build(), HttpMethod.GET, token);
            String[] layers = {"data", "securities"};
            List<FinamSecuritiesResponseDto> dtoList = dataParser.parseObjectsList(response, FinamSecuritiesResponseDto.class, layers);
            if (dtoList.isEmpty()) {
                return Collections.emptyList();
            } else {
                return dtoList.stream().map(FinamSecuritiesResponseDto::toStockClass).toList();
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
    public Optional<Futures> getFutures(String ticker) {
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