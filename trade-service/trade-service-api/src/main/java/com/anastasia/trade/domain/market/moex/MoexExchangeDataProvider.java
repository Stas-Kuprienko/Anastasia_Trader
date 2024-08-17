package com.anastasia.trade.domain.market.moex;

import com.anastasia.trade.domain.market.ExchangeDataProvider;
import com.anastasia.trade.domain.market.moex.converters.FuturesConverter;
import com.anastasia.trade.domain.market.moex.converters.StocksConverter;
import com.anastasia.trade.entities.ExchangeMarket;
import com.anastasia.trade.entities.markets.Futures;
import com.anastasia.trade.entities.markets.Securities;
import com.anastasia.trade.entities.markets.Stock;
import com.anastasia.trade.service.DataCacheService;
import com.anastasia.trade.utils.ApiDataParser;
import com.anastasia.trade.utils.GetRequestParametersBuilder;
import com.anastasia.trade.utils.MyRestClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import static com.anastasia.trade.domain.market.moex.MoexApiClient.Engine;
import static com.anastasia.trade.domain.market.moex.MoexApiClient.Market;
import static com.anastasia.trade.domain.market.moex.MoexExchangeDataProvider.Args.*;

@Service("moexExchangeData")
public class MoexExchangeDataProvider implements ExchangeDataProvider {

    private final String STOCK_URL;
    private final String STOCKS_URL;
    private final String FUTURES_URL;
    private final String FUTURES_LIST_URL;

    private final ApiDataParser dataParser;
    private final DataCacheService dataCacheService;

    private final MyRestClient myRestClient;


    @Autowired
    public MoexExchangeDataProvider(@Qualifier("jsonParser") ApiDataParser dataParser,
                                    DataCacheService dataCacheService, MyRestClient myRestClient) {
        this.dataParser = dataParser;
        this.dataCacheService = dataCacheService;
        this.myRestClient = myRestClient;
        this.STOCK_URL = stockUrl();
        this.STOCKS_URL = stocksUrl();
        this.FUTURES_URL = futuresUrl();
        this.FUTURES_LIST_URL = futuresListUrl();
    }


    @Override
    public ExchangeMarket getExchange() {
        return ExchangeMarket.MOEX;
    }

    @Override
    public Optional<Stock> getStock(String ticker) {
        String key = getExchange().toString() + ':' + Stock.class.getSimpleName();
        Stock stock = dataCacheService.getAndParseFromJson(key, ticker, Stock.class);
        if (stock == null) {
            String uri = String.format(STOCK_URL, ticker);
            List<List<Object[]>> dtoLists = getSecurity(uri);
            var dto = dtoLists.get(0);
            var marketData = dtoLists.get(1);
            if (dto.isEmpty() || marketData.isEmpty()) {
                return Optional.empty();
            } else {
                stock = StocksConverter.moexDtoToStock(dto.getFirst(), marketData.getFirst());
            }
        }
        return Optional.of(stock);
    }

    @Override
    public List<Stock> getStocks() {
        return getStocks(SortByColumn.NONE, null);
    }

    @Override
    public List<Stock> getStocks(SortByColumn sortByColumn, SortOrder sortOrder) {
        boolean toSort = sortByColumn.equals(SortByColumn.TRADE_VOLUME) && sortOrder.equals(SortOrder.desc);
        //TODO sorting criteria

        String key = getExchange().toString() + ':' + Stock.class.getSimpleName();
        List<Stock> stocks = dataCacheService.getAndParseListForKeyFromJson(key, Stock.class);
        if (stocks == null || stocks.isEmpty()) {
            GetRequestParametersBuilder query = new GetRequestParametersBuilder(STOCKS_URL);
            List<List<Object[]>> dtoLists = getSecurities(query, sortByColumn, sortOrder);
            var dto = dtoLists.get(0);
            var marketData = dtoLists.get(1);
            if (dto.isEmpty() || marketData.isEmpty()) {
                return Collections.emptyList();
            } else {
                stocks = StocksConverter.moexDtoToStocks(dto, marketData);
                putSecuritiesToCache(stocks);
                if (toSort) {
                    stocks = stocks.stream().sorted().toList();
                }
            }
        }
        return stocks;
    }

    @Override
    public Optional<Futures> getFutures(String ticker) {
        String key = getExchange().toString() + ':' + Futures.class.getSimpleName();
        Futures futures = dataCacheService.getAndParseFromJson(key, ticker, Futures.class);
        if (futures == null) {
            String uri = String.format(FUTURES_URL, ticker);
            List<List<Object[]>> dtoLists = getSecurity(uri);
            var dto = dtoLists.get(0);
            var marketData = dtoLists.get(1);
            if (dto.isEmpty() || marketData.isEmpty()) {
                return Optional.empty();
            } else {
                futures = FuturesConverter.moexDtoToFutures(dto.getFirst(), marketData.getFirst());
            }
        }
        return Optional.of(futures);
    }

    @Override
    public List<Futures> getFuturesList() {
        return getFuturesList(SortByColumn.NONE, null);
    }

    @Override
    public List<Futures> getFuturesList(SortByColumn sortByColumn, SortOrder sortOrder) {
        boolean toSort = sortByColumn.equals(SortByColumn.TRADE_VOLUME) && sortOrder.equals(SortOrder.desc);
        //TODO sorting criteria

        String key = getExchange().toString() + ':' + Futures.class.getSimpleName();
        List<Futures> futuresList = dataCacheService.getAndParseListForKeyFromJson(key, Futures.class);
        if (futuresList == null || futuresList.isEmpty()) {
            GetRequestParametersBuilder query = new GetRequestParametersBuilder(FUTURES_LIST_URL);
            List<List<Object[]>> dtoLists = getSecurities(query, sortByColumn, sortOrder);
            var dto = dtoLists.get(0);
            var marketData = dtoLists.get(1);
            if (dto.isEmpty() || marketData.isEmpty()) {
                return Collections.emptyList();
            } else {
                futuresList = FuturesConverter.moexDtoToFuturesList(dto, marketData);
                putSecuritiesToCache(futuresList);
                if (toSort) {
                    futuresList = futuresList.stream().sorted().toList();
                }
            }
        }
        return futuresList;
    }


    private List<List<Object[]>> getSecurity(String uri) {
        GetRequestParametersBuilder query = new GetRequestParametersBuilder(uri);
        query.add(marketdata.toString(), 1);
        return getListOfLists(query.build());
    }

    private List<List<Object[]>> getSecurities(GetRequestParametersBuilder query, SortByColumn sortByColumn, SortOrder sortOrder) {
        query.add(marketdata.toString(), 1);
        query.add(leaders.toString(), 1);
        if (!sortByColumn.equals(SortByColumn.NONE)) {
            String sortColumn = switch (sortByColumn) {
                case TRADE_VOLUME -> SortingColumns.VALTODAY.toString();
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
        String response = myRestClient.get(uri, null, String.class).getBody();
        String[] layers = {"securities", "data"};
        List<Object[]> dto = dataParser.parseObjectsList(response, Object[].class, layers);
        layers = new String[]{"marketdata", "data"};
        List<Object[]> marketData = dataParser.parseObjectsList(response, Object[].class, layers);
        return List.of(dto, marketData);
    }

    private void putSecuritiesToCache(List<? extends Securities> securitiesList) {
        String key = getExchange().toString();
        for(Securities s : securitiesList) {
            dataCacheService.putAsJson(key + ':' + s.getClass().getSimpleName(), s.getTicker(), s);
        }
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

    enum SortingColumns {

        VALTODAY
    }
}