package com.stanislav.trade.domain.market.moex;

import com.stanislav.trade.domain.market.ExchangeData;
import com.stanislav.trade.domain.market.ExchangeDataStorage;
import com.stanislav.trade.domain.market.moex.converters.FuturesConverter;
import com.stanislav.trade.domain.market.moex.converters.MarketData;
import com.stanislav.trade.domain.market.moex.converters.StocksConverter;
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
import static com.stanislav.trade.domain.market.moex.MoexApiClient.Engine;
import static com.stanislav.trade.domain.market.moex.MoexApiClient.Market;
import static com.stanislav.trade.domain.market.moex.MoexExchangeData.Args.*;

@Service("moexExchangeData")
public class MoexExchangeData implements ExchangeData {

    private final String STOCK_URL;
    private final String STOCKS_URL;
    private final String FUTURES_URL;
    private final String FUTURES_LIST_URL;

    private final ApiDataParser dataParser;
    private final RestConsumer restConsumer;
    private final ExchangeDataStorage exchangeDataStorage;


    @Autowired
    public MoexExchangeData(@Qualifier("jsonParser") ApiDataParser dataParser,
                            RestConsumer restConsumer, ExchangeDataStorage exchangeDataStorage) {
        this.dataParser = dataParser;
        this.restConsumer = restConsumer;
        this.exchangeDataStorage = exchangeDataStorage;
        this.STOCK_URL = stockUrl();
        this.STOCKS_URL = stocksUrl();
        this.FUTURES_URL = futuresUrl();
        this.FUTURES_LIST_URL = futuresListUrl();
    }


    @Override
    public Optional<Stock> getStock(String ticker) {
        Optional<Stock> stock = exchangeDataStorage.getStock(ticker);
        if (stock.isPresent()) {
            return stock;
        }
        String uri = String.format(STOCK_URL, ticker);
        List<Object[]> dto = getSecurity(uri);
        if (dto.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(StocksConverter.moexDtoToStock(dto.getFirst()));
        }
    }

    @Override
    public List<Stock> getStocks() {
        List<Stock> stocks = exchangeDataStorage.getAllStocks();
        if (!stocks.isEmpty()) {
            return stocks;
        }
        return getStocks(SortByColumn.NONE, null);
    }

    @Override
    public List<Stock> getStocks(SortByColumn sortByColumn, SortOrder sortOrder) {
        GetQueryBuilder query = new GetQueryBuilder(STOCKS_URL);
        List<Object[]> dto = getSecurities(query, sortByColumn, sortOrder);
        if (dto.isEmpty()) {
            return Collections.emptyList();
        } else {
            var list = StocksConverter.moexDtoToStocks(dto);
            exchangeDataStorage.addStockList(list);
            return list;
        }
    }

    @Override
    public Optional<Futures> getFutures(String ticker) {
        String uri = String.format(FUTURES_URL, ticker);
        List<Object[]> dto = getSecurity(uri);
        if (dto.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(FuturesConverter.moexDtoToFutures(dto.getFirst()));
        }
    }

    @Override
    public List<Futures> getFuturesList() {
        return getFuturesList(SortByColumn.NONE, null);
    }

    @Override
    public List<Futures> getFuturesList(SortByColumn sortByColumn, SortOrder sortOrder) {
        GetQueryBuilder query = new GetQueryBuilder(FUTURES_LIST_URL);
        List<Object[]> dto = getSecurities(query, sortByColumn, sortOrder);
        if (dto.isEmpty()) {
            return Collections.emptyList();
        } else {
            return FuturesConverter.moexDtoToFuturesList(dto);
        }
    }


    private List<Object[]> getSecurity(String uri) {
        GetQueryBuilder query = new GetQueryBuilder(uri);
        query.add(marketdata.toString(), 1);
        String response = restConsumer.doRequest(query.build(), HttpMethod.GET);
        String[] layers = {"securities", "data"};
        return dataParser.parseObjectsList(response, Object[].class, layers);
    }

    private List<Object[]> getSecurities(GetQueryBuilder query, SortByColumn sortByColumn, SortOrder sortOrder) {
        query.add(marketdata.toString(), 1);
        query.add(leaders.toString(), 1);
        if (!sortByColumn.equals(SortByColumn.NONE)) {
            String sortColumn = switch (sortByColumn) {
                case TICKER -> MarketData.SECID.toString();
                case TRADE_VOLUME -> MarketData.VALTODAY.toString();
                case CHANGE_PERCENT -> MarketData.LASTCHANGEPRCNT.toString();
                case null, default -> throw new IllegalArgumentException(sortByColumn.toString());
            };
            query.add(sort_column.toString(), sortColumn.toLowerCase());
            if (sortOrder != null) {
                query.add(sort_order.toString(), sortOrder);
            }
        }
        String response = restConsumer.doRequest(query.build(), HttpMethod.GET);
        String[] layers = {"securities", "data"};
        return dataParser.parseObjectsList(response, Object[].class, layers);
    }

    private String stockUrl() {
        return MoexApiClient.moexApiJsonClient()
                .engines()
                .engine(Engine.stock)
                .markets()
                .market(Market.shares)
                .boards()
                .board(MoexApiClient.Board.tqbr)
                .securities()
                .parameterFormat()
                .build();
    }

    private String stocksUrl() {
        return MoexApiClient.moexApiJsonClient()
                .engines()
                .engine(Engine.stock)
                .markets()
                .market(Market.shares)
                .boards()
                .board(MoexApiClient.Board.tqbr)
                .securities()
                .build();
    }

    private String futuresListUrl() {
        return MoexApiClient.moexApiJsonClient()
                .engines()
                .engine(Engine.futures)
                .markets()
                .market(Market.forts)
                .securities()
                .build();
    }

    private String futuresUrl() {
        return MoexApiClient.moexApiJsonClient()
                .engines()
                .engine(Engine.futures)
                .markets()
                .market(Market.forts)
                .securities()
                .parameterFormat()
                .build();
    }


    enum Args {

        marketdata,
        boardergroup,
        leaders,
        sort_column,
        sort_order
    }
}