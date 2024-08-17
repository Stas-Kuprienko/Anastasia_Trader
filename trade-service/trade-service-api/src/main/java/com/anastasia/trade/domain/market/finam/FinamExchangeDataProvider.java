/*
 * Stanislav Kuprienko *** Omsk, Russia
 */

package com.anastasia.trade.domain.market.finam;

import com.anastasia.trade.domain.market.ExchangeDataProvider;
import com.anastasia.trade.domain.market.finam.dto.FinamSecuritiesResponseDto;
import com.anastasia.trade.entities.Board;
import com.anastasia.trade.entities.ExchangeMarket;
import com.anastasia.trade.entities.markets.Futures;
import com.anastasia.trade.entities.markets.Stock;
import com.anastasia.trade.utils.ApiDataParser;
import com.anastasia.trade.utils.GetRequestParametersBuilder;
import com.anastasia.trade.utils.MyRestClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.HttpStatusCodeException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import static com.anastasia.trade.domain.market.finam.FinamExchangeDataProvider.Args.*;
import static com.anastasia.trade.domain.market.finam.FinamExchangeDataProvider.Resource.*;

//@Service("finamExchangeData")
public class FinamExchangeDataProvider implements ExchangeDataProvider {

    private final String token;
    private final String resource;
    private final ApiDataParser dataParser;
    private final MyRestClient myRestClient;


    @Autowired
    public FinamExchangeDataProvider(MyRestClient myRestClient,
                                     @Value("${api.finam}") String resource,
                                     @Value("${token}") String token,
                                     @Qualifier("jsonParser") ApiDataParser dataParser) {
        this.dataParser = dataParser;
        this.myRestClient = myRestClient;
        this.token = token;
        this.resource = resource;
    }


    @Override
    public ExchangeMarket getExchange() {
        return ExchangeMarket.MOEX;
    }

    @Override
    public Optional<Stock> getStock(String ticker) {
        GetRequestParametersBuilder query = new GetRequestParametersBuilder(resource + SECURITIES.value);
        query.add(BOARD.value, Board.TQBR).add(SEC_CODE.value, ticker);
        try {
            String response = myRestClient.get(query.build(), token, String.class).getBody();
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
        GetRequestParametersBuilder query = new GetRequestParametersBuilder(resource + SECURITIES.value);
        query.add(BOARD.value, Board.TQBR);
        try {
            String response = myRestClient.get(query.build(), token, String.class).getBody();
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