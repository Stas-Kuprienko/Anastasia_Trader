package com.stanislav.trade.domain.service.moex;

import com.stanislav.trade.domain.service.ExchangeData;
import com.stanislav.trade.entities.Board;
import com.stanislav.trade.entities.markets.Futures;
import com.stanislav.trade.entities.markets.Stock;
import com.stanislav.trade.utils.ApiDataParser;
import com.stanislav.trade.utils.GetQueryBuilder;
import com.stanislav.trade.utils.RestConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import java.util.Collections;
import java.util.List;

import static com.stanislav.trade.domain.service.moex.MoexExchangeData.Args.*;
import static com.stanislav.trade.domain.service.moex.MoexExchangeData.resources.*;
import static com.stanislav.trade.domain.service.moex.MoexExchangeData.markets.*;
import static com.stanislav.trade.domain.service.moex.MoexExchangeData.engines.*;
import static com.stanislav.trade.domain.service.moex.MoexSecuritiesParseTool.*;

@Service("moexExchangeData")
public class MoexExchangeData implements ExchangeData {

    //TODO redo with builder

    private final String STOCKS_URL;

    private final ApiDataParser dataParser;
    private final RestConsumer restConsumer;


    public MoexExchangeData(@Autowired @Qualifier("jsonParser") ApiDataParser dataParser,
                            @Autowired RestConsumer restConsumer,
                            @Value("${api.moex}") String resource) {
        this.dataParser = dataParser;
        this.restConsumer = restConsumer;
        this.restConsumer.setResource(resource);
        this.STOCKS_URL = MoexApiClient.moexApiJsonClient()
                .history()
                .engines()
                .stock()
                .markets()
                .shares()
                .securities().build();
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
        GetQueryBuilder query = new GetQueryBuilder(STOCKS_URL);
        query.add(boardergroup.toString(), Board.TQBR);
        if (!sortByColumn.equals(SortByColumn.NONE)) {
            String s = switch (sortByColumn) {
                case TICKER -> SECID.toString();
                case TRADE_VOLUME -> VALUE.toString();
                case null, default -> throw new IllegalArgumentException(sortByColumn.toString());
            };
            query.add(sort_column.toString(), s);
            query.add(sort_order.toString(), sortOrder);
        }
        String response = restConsumer.doRequest(query.build(), HttpMethod.GET);
        String[] layers = {"history", "data"};
        List<Object[]> objects = dataParser.parseObjectsList(response, Object[].class, layers);
        if (objects.isEmpty()) {
            return Collections.emptyList();
        } else {
            return convertMoexDtoToStocks(objects);
        }
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

        boardergroup,
        sort_column,
        sort_order
    }

    enum resources {
        engines,
        markets,
        history,
        securities,
        statistics
    }

    enum engines {

        stock,
        currency,
        futures
    }

    enum markets {

        shares,
        bonds,
        forts
    }
}

// https://iss.moex.com/iss/history/engines/stock/markets/shares/securities.json?boardgroup=tqbr&sort_column=value&sort_order=desc