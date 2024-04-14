package com.stanislav.domain.trading.finam;

import com.stanislav.domain.trading.MarketData;
import com.stanislav.domain.trading.finam.securities_dto.FinamSecuritiesResponse;
import com.stanislav.entities.Board;
import com.stanislav.entities.candles.DayCandles;
import com.stanislav.entities.candles.IntraDayCandles;
import com.stanislav.entities.markets.Stock;
import com.stanislav.entities.user.Account;
import com.stanislav.web.utils.ApiDataParser;
import com.stanislav.web.utils.GetQueryBuilder;
import com.stanislav.web.utils.RestConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.stanislav.domain.trading.finam.FinamMarketData.Args.*;
import static com.stanislav.domain.trading.finam.FinamMarketData.Resource.*;

@Service("finamMarketData")
public class FinamMarketData implements MarketData {

    private final ApiDataParser dataParser;
    private final RestConsumer restConsumer;


    public FinamMarketData(@Autowired @Qualifier("jsonParser") ApiDataParser dataParser,
                           @Autowired RestConsumer restConsumer,
                           @Value("${broker.finam}") String resource) {
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

    @Override
    public DayCandles getDayCandles(@NonNull Account account, @NonNull String ticker, @NonNull String timeFrame,
                                    @NonNull LocalDate from, @NonNull LocalDate to, Integer interval) {
        GetQueryBuilder query = new GetQueryBuilder(DAY_CANDLES.value);
        query.add(SECURITY_BOARD.value, Board.TQBR)
                .add(SECURITY_CODE.value, ticker)
                .add(TIME_FRAME.value, timeFrame)
                .add(INTERVAL_FROM.value, from)
                .add(INTERVAL_TO.value, to)
                .add(INTERVAL_COUNT.value, interval);
        try {
            String response = restConsumer.doRequest(query.build(), HttpMethod.GET, account.getToken());
            String[] layers = {"data"};
            return dataParser.parseObject(response, DayCandles.class, layers);
        } catch (HttpStatusCodeException e) {
            e.printStackTrace();
            //TODO
            return new DayCandles(new DayCandles.Candle[0]);
        }
    }

    @Override
    public IntraDayCandles getIntraDayCandles(@NonNull Account account, @NonNull String ticker, @NonNull String timeFrame,
                                              @NonNull LocalDateTime from, @NonNull LocalDateTime to, Integer interval) {
        GetQueryBuilder query = new GetQueryBuilder(INTRA_DAY.value);
        //TODO parameters validation
        query.add(SECURITY_BOARD.value, Board.TQBR)
                .add(SECURITY_CODE.value, ticker)
                .add(TIME_FRAME.value, timeFrame)
                .add(INTERVAL_FROM.value, from)
                .add(INTERVAL_TO.value, to)
                .add(INTERVAL_COUNT.value, interval);
        try {
            String response = restConsumer.doRequest(query.build(), HttpMethod.GET, account.getToken());
            String[] layers = {"data"};
            return dataParser.parseObject(response, IntraDayCandles.class, layers);
        } catch (HttpStatusCodeException e) {
            e.printStackTrace();
            //TODO
            return new IntraDayCandles(new IntraDayCandles.Candle[0]);
        }
    }


    enum Args {
        BOARD("Board"),
        SEC_CODE("Seccode"),
        SECURITY_BOARD("SecurityBoard"),
        SECURITY_CODE("SecurityCode"),
        TIME_FRAME("TimeFrame"),
        INTERVAL_FROM("Interval.From"),
        INTERVAL_TO("Interval.To"),
        INTERVAL_COUNT("Interval.Count");


        final String value;
        Args(String value) {
            this.value = value;
        }
    }

    enum Resource {

        SECURITIES("securities"),
        DAY_CANDLES("day-candles"),
        INTRA_DAY("intraday-candles");

        final String value;
        Resource(String value) {
            this.value = value;
        }
    }
}