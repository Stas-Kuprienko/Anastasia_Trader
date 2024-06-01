package com.stanislav.trade.domain.service.moex;

import com.stanislav.trade.domain.service.ExchangeData;
import com.stanislav.trade.domain.service.moex.converters.FuturesListConverter;
import com.stanislav.trade.domain.service.moex.converters.SecuritiesConverter;
import com.stanislav.trade.domain.service.moex.converters.StockListConverter;
import com.stanislav.trade.entities.markets.Futures;
import com.stanislav.trade.entities.markets.Stock;
import com.stanislav.trade.utils.ApiDataParser;
import com.stanislav.trade.utils.GetQueryBuilder;
import com.stanislav.trade.utils.RestConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import static com.stanislav.trade.domain.service.moex.MoexApiClient.Engine;
import static com.stanislav.trade.domain.service.moex.MoexApiClient.Market;
import static com.stanislav.trade.domain.service.moex.MoexExchangeData.Args.*;

@Service("moexExchangeData")
public class MoexExchangeData implements ExchangeData {

    private final String STOCKS_URL;
    private final String FUTURES_URL;

    private final ApiDataParser dataParser;
    private final RestConsumer restConsumer;


    public MoexExchangeData(@Autowired @Qualifier("jsonParser") ApiDataParser dataParser,
                            @Autowired RestConsumer restConsumer) {
        this.dataParser = dataParser;
        this.restConsumer = restConsumer;
        this.STOCKS_URL = stocksUrl();
        this.FUTURES_URL = futuresUrl();
    }


    @Override
    public Optional<Stock> getStock(String ticker) {
        return null;
    }

    @Override
    public List<Stock> getStocks() {
        return getStocks(SortByColumn.NONE, null);
    }

    @Override
    public List<Stock> getStocks(SortByColumn sortByColumn, SortOrder sortOrder) {
        GetQueryBuilder query = new GetQueryBuilder(STOCKS_URL);
        List<Object[]> dto = getSecurities(query, sortByColumn, sortOrder);
        if (dto.isEmpty()) {
            return Collections.emptyList();
        } else {
            return StockListConverter.moexDtoToStocks(dto);
        }
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
        GetQueryBuilder query = new GetQueryBuilder(FUTURES_URL);
        List<Object[]> dto = getSecurities(query, sortByColumn, sortOrder);
        if (dto.isEmpty()) {
            return Collections.emptyList();
        } else {
            return FuturesListConverter.moexDtoToFutures(dto);
        }
    }


    private List<Object[]> getSecurities(GetQueryBuilder query, SortByColumn sortByColumn, SortOrder sortOrder) {
        query.add(marketdata.toString(), 1);
        query.add(leaders.toString(), 1);
        if (!sortByColumn.equals(SortByColumn.NONE)) {
            String sortColumn = switch (sortByColumn) {
                case TICKER -> SecuritiesConverter.SECID.toString();
                case TRADE_VOLUME -> SecuritiesConverter.VALTODAY.toString();
                case CHANGE_PERCENT -> SecuritiesConverter.LASTCHANGEPRCNT.toString();
                case null, default -> throw new IllegalArgumentException(sortByColumn.toString());
            };
            query.add(sort_column.toString(), sortColumn.toLowerCase());
            query.add(sort_order.toString(), sortOrder);
        }
        String response = restConsumer.doRequest(query.build(), HttpMethod.GET);
        String[] layers = {"securities", "data"};
        return dataParser.parseObjectsList(response, Object[].class, layers);
    }

    private String stocksUrl() {
        return MoexApiClient.moexApiJsonClient()
                .engines()
                .engine(Engine.stock)
                .markets()
                .market(Market.shares)
                .boards()
                .board(MoexApiClient.Board.tqbr)
                .securities().build();
    }

    private String futuresUrl() {
        return MoexApiClient.moexApiJsonClient()
                .engines()
                .engine(Engine.futures)
                .markets()
                .market(Market.forts)
                .securities().build();
    }


    enum Args {

        marketdata,
        boardergroup,
        leaders,
        sort_column,
        sort_order
    }
}

// https://iss.moex.com/iss/engines/stock/markets/shares/boards/tqbr/securities.json?marketdata=1&sort_column=valtoday&sort_order=desc&marketprice_board=1
// https://iss.moex.com/iss/history/engines/stock/markets/shares/securities.json?boardgroup=tqbr&sort_column=value&sort_order=desc