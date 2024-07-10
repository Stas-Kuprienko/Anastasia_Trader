package com.stanislav.trade.domain.market.moex;

import com.stanislav.trade.domain.market.ExchangeData;
import com.stanislav.trade.domain.market.ExchangeDataCache;
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
import java.time.LocalDateTime;
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
    private final ExchangeDataCache exchangeDataCache;


    @Autowired
    public MoexExchangeData(@Qualifier("jsonParser") ApiDataParser dataParser,
                            RestConsumer restConsumer, ExchangeDataCache exchangeDataCache) {
        this.dataParser = dataParser;
        this.restConsumer = restConsumer;
        this.exchangeDataCache = exchangeDataCache;
        this.STOCK_URL = stockUrl();
        this.STOCKS_URL = stocksUrl();
        this.FUTURES_URL = futuresUrl();
        this.FUTURES_LIST_URL = futuresListUrl();
    }


    @Override
    public Optional<Stock> getStock(String ticker) {
        String uri = String.format(STOCK_URL, ticker);
        List<List<Object[]>> dtoLists = getSecurity(uri);
        var dto = dtoLists.get(0);
        var marketData = dtoLists.get(1);
        if (dto.isEmpty() || marketData.isEmpty()) {
            return Optional.empty();
        } else {
            var stock = Optional.of(StocksConverter.moexDtoToStock(dto.getFirst(), marketData.getFirst()));
            stock.ifPresent(exchangeDataCache::add);
            return stock;
        }
    }

    @Override
    public List<Stock> getStocks() {
        return getStocks(SortByColumn.NONE, null);
    }

    @Override
    public List<Stock> getStocks(SortByColumn sortByColumn, SortOrder sortOrder) {
        boolean toSort = sortByColumn.equals(SortByColumn.TRADE_VOLUME) && sortOrder.equals(SortOrder.desc);
        //TODO sorting criteria
        if (isUpToDate(exchangeDataCache.getStocksUpdated())) {
            List<Stock> stocks = exchangeDataCache.getAllStocks();
            if (!stocks.isEmpty()) {
                if (toSort) {
                    stocks = stocks.stream().sorted().toList();
                }
                return stocks;
            }
        }
        GetQueryBuilder query = new GetQueryBuilder(STOCKS_URL);
        List<List<Object[]>> dtoLists = getSecurities(query, sortByColumn, sortOrder);
        var dto = dtoLists.get(0);
        var marketData = dtoLists.get(1);
        if (dto.isEmpty() || marketData.isEmpty()) {
            return Collections.emptyList();
        } else {
            List<Stock> stocks = StocksConverter.moexDtoToStocks(dto, marketData);
            exchangeDataCache.addStockList(stocks);
            exchangeDataCache.stocksUpdated();
            if (toSort) {
                stocks = stocks.stream().sorted().toList();
            }
            return stocks;
        }
    }

    @Override
    public Optional<Futures> getFutures(String ticker) {
        String uri = String.format(FUTURES_URL, ticker);
        List<List<Object[]>> dtoLists = getSecurity(uri);
        var dto = dtoLists.get(0);
        var marketData = dtoLists.get(1);
        if (dto.isEmpty() || marketData.isEmpty()) {
            return Optional.empty();
        } else {
            var futures = Optional.of(FuturesConverter.moexDtoToFutures(dto.getFirst(), marketData.getFirst()));
            futures.ifPresent(exchangeDataCache::add);
            return futures;
        }
    }

    @Override
    public List<Futures> getFuturesList() {
        return getFuturesList(SortByColumn.NONE, null);
    }

    @Override
    public List<Futures> getFuturesList(SortByColumn sortByColumn, SortOrder sortOrder) {
        boolean toSort = sortByColumn.equals(SortByColumn.TRADE_VOLUME) && sortOrder.equals(SortOrder.desc);
        //TODO sorting criteria
        if (isUpToDate(exchangeDataCache.getFuturesUpdated())) {
            List<Futures> futuresList = exchangeDataCache.getAllFutures();
            if (!futuresList.isEmpty()) {
                if (toSort) {
                    futuresList = futuresList.stream().sorted().toList();
                }
                return futuresList;
            }
        }
        GetQueryBuilder query = new GetQueryBuilder(FUTURES_LIST_URL);
        List<List<Object[]>> dtoLists = getSecurities(query, sortByColumn, sortOrder);
        var dto = dtoLists.get(0);
        var marketData = dtoLists.get(1);
        if (dto.isEmpty() || marketData.isEmpty()) {
            return Collections.emptyList();
        } else {
            List<Futures> futuresList = FuturesConverter.moexDtoToFuturesList(dto, marketData);
            exchangeDataCache.addFuturesList(futuresList);
            exchangeDataCache.futuresUpdated();
            if (toSort) {
                futuresList = futuresList.stream().sorted().toList();
            }
            return futuresList;
        }
    }


    private List<List<Object[]>> getSecurity(String uri) {
        GetQueryBuilder query = new GetQueryBuilder(uri);
        query.add(marketdata.toString(), 1);
        return getListOfLists(query.build());
    }

    private List<List<Object[]>> getSecurities(GetQueryBuilder query, SortByColumn sortByColumn, SortOrder sortOrder) {
        query.add(marketdata.toString(), 1);
        query.add(leaders.toString(), 1);
        if (!sortByColumn.equals(SortByColumn.NONE)) {
            String sortColumn = switch (sortByColumn) {
//                case TICKER -> MarketData.SECID.toString();
                case TRADE_VOLUME -> MarketData.VALTODAY.toString();
//                case CHANGE_PERCENT -> MarketData.LASTCHANGEPRCNT.toString();
                case null, default -> throw new IllegalArgumentException(sortByColumn.toString());
            };
            query.add(sort_column.toString(), sortColumn.toLowerCase());
            if (sortOrder != null) {
                query.add(sort_order.toString(), sortOrder);
            }
        }
        return getListOfLists(query.build());
    }

    private List<List<Object[]>> getListOfLists(String uri) {
        String response = restConsumer.doRequest(uri, HttpMethod.GET);
        String[] layers = {"securities", "data"};
        List<Object[]> dto = dataParser.parseObjectsList(response, Object[].class, layers);
        layers = new String[]{"marketdata", "data"};
        List<Object[]> marketData = dataParser.parseObjectsList(response, Object[].class, layers);
        return List.of(dto, marketData);
    }

    private boolean isUpToDate(LocalDateTime time) {
        if (time == null) {
            return false;
        }
        return LocalDateTime.now().isBefore(time.plusHours(24));
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