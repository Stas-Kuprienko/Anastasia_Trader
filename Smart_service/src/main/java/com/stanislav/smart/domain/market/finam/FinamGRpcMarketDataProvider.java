package com.stanislav.smart.domain.market.finam;

import com.google.protobuf.Timestamp;
import com.google.type.Date;
import com.stanislav.smart.domain.entities.Board;
import com.stanislav.smart.domain.entities.TimeFrame;
import com.stanislav.smart.domain.entities.candles.DayCandles;
import com.stanislav.smart.domain.entities.candles.IntraDayCandles;
import com.stanislav.smart.domain.market.finam.candles.FinamDayCandlesProxy;
import com.stanislav.smart.domain.market.finam.candles.FinamIntraDayCandlesProxy;
import com.stanislav.smart.service.grpc_impl.GRpcClient;
import com.stanislav.smart.domain.market.MarketDataProvider;
import grpc.tradeapi.v1.CandlesGrpc;
import proto.tradeapi.v1.Candles;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class FinamGRpcMarketDataProvider implements MarketDataProvider {

    private final CandlesGrpc.CandlesBlockingStub stub;


    public FinamGRpcMarketDataProvider(GRpcClient client) {
        this.stub = CandlesGrpc.newBlockingStub(client.getChannel())
                .withCallCredentials(client.getAuthenticator());
    }


    @Override
    public DayCandles getStockDayCandles(String ticker, TimeFrame.Day timeFrame, LocalDate to, Integer count) {
        Date dateTo = Date.newBuilder()
                .setDay(to.getDayOfMonth())
                .setMonth(to.getMonthValue())
                .setYear(to.getYear()).build();

        Candles.DayCandleInterval interval = Candles.DayCandleInterval.newBuilder()
                .setTo(dateTo)
                .setCount(count).build();

        return buildDayCandlesProxy(getDayCandlesResult(ticker, Board.TQBR, timeFrame, interval));
    }

    @Override
    public DayCandles getStockDayCandles(String ticker, TimeFrame.Day timeFrame, LocalDate from, LocalDate to) {
        Date dateFrom = Date.newBuilder()
                .setDay(from.getDayOfMonth())
                .setMonth(from.getMonthValue())
                .setYear(from.getYear()).build();

        Date dateTo = Date.newBuilder()
                .setDay(to.getDayOfMonth())
                .setMonth(to.getMonthValue())
                .setYear(to.getYear()).build();

        Candles.DayCandleInterval interval = Candles.DayCandleInterval.newBuilder()
                .setFrom(dateFrom)
                .setTo(dateTo).build();

        return buildDayCandlesProxy(getDayCandlesResult(ticker, Board.TQBR, timeFrame, interval));
    }

    @Override
    public IntraDayCandles getStockIntraDayCandles(String ticker, TimeFrame.IntraDay timeFrame,
                                                   LocalDateTime to, Integer count) {
        //TODO need to mind the time format!
        Timestamp timeTo = Timestamp.newBuilder()
                .setSeconds(to.toEpochSecond(ZoneOffset.UTC))
                .setNanos(to.getNano()).build();

        Candles.IntradayCandleInterval interval = Candles.IntradayCandleInterval.newBuilder()
                .setTo(timeTo)
                .setCount(count).build();

        return buildIntraDayCandlesProxy(getIntradayCandlesResult(ticker, Board.TQBR, timeFrame, interval));
    }

    @Override
    public IntraDayCandles getStockIntraDayCandles(String ticker, TimeFrame.IntraDay timeFrame,
                                                   LocalDateTime from, LocalDateTime to) {
        //TODO need to mind the time format!
        Timestamp timeFrom = Timestamp.newBuilder()
                .setSeconds(from.toEpochSecond(ZoneOffset.UTC))
                .setNanos(from.getNano()).build();

        Timestamp timeTo = Timestamp.newBuilder()
                .setSeconds(to.toEpochSecond(ZoneOffset.UTC))
                .setNanos(to.getNano()).build();

        Candles.IntradayCandleInterval interval = Candles.IntradayCandleInterval.newBuilder()
                .setFrom(timeFrom)
                .setTo(timeTo).build();

        return buildIntraDayCandlesProxy(getIntradayCandlesResult(ticker, Board.TQBR, timeFrame, interval));
    }

    @Override
    public DayCandles getFuturesDayCandles(String ticker, TimeFrame.Day timeFrame, LocalDate to, Integer count) {
        return null;
    }

    @Override
    public DayCandles getFuturesDayCandles(String ticker, TimeFrame.Day timeFrame, LocalDate from, LocalDate to) {
        return null;
    }

    @Override
    public IntraDayCandles getFuturesIntraDayCandles(String ticker, TimeFrame.IntraDay timeFrame,
                                                     LocalDateTime to, Integer count) {
        return null;
    }

    @Override
    public IntraDayCandles getFuturesIntraDayCandles(String ticker, TimeFrame.IntraDay timeFrame,
                                                     LocalDateTime from, LocalDateTime to) {
        return null;
    }


    private Candles.GetDayCandlesResult getDayCandlesResult(String ticker, Board board,
                                                            TimeFrame.Day timeFrame, Candles.DayCandleInterval interval) {
        
        Candles.GetDayCandlesRequest request = Candles.GetDayCandlesRequest.newBuilder()
                .setSecurityCode(ticker)
                .setSecurityBoard(board.name())
                .setInterval(interval)
                .setTimeFrame(TimeFrame.toProto(timeFrame))
                .build();

        return stub.getDayCandles(request);
    }

    private Candles.GetIntradayCandlesResult getIntradayCandlesResult(String ticker, Board board,
                                                                      TimeFrame.IntraDay timeFrame, Candles.IntradayCandleInterval interval) {
        Candles.GetIntradayCandlesRequest request = Candles.GetIntradayCandlesRequest.newBuilder()
                .setSecurityCode(ticker)
                .setSecurityBoard(board.name())
                .setInterval(interval)
                .setTimeFrame(TimeFrame.toProto(timeFrame))
                .build();

        return stub.getIntradayCandles(request);
    }

    private FinamDayCandlesProxy buildDayCandlesProxy(Candles.GetDayCandlesResult result) {

        FinamDayCandlesProxy.FinamDayCandleProxy[] dayCandles = result
                .getCandlesList()
                .stream()
                .map(FinamDayCandlesProxy.FinamDayCandleProxy::new)
                .toList()
                .toArray(new FinamDayCandlesProxy.FinamDayCandleProxy[]{});

        return new FinamDayCandlesProxy(dayCandles);
    }

    private FinamIntraDayCandlesProxy buildIntraDayCandlesProxy(Candles.GetIntradayCandlesResult result) {

        FinamIntraDayCandlesProxy.FinamIntraDayCandleProxy[] intraDayCandles = result
                .getCandlesList()
                .stream().map(FinamIntraDayCandlesProxy.FinamIntraDayCandleProxy::new)
                .toList()
                .toArray(new FinamIntraDayCandlesProxy.FinamIntraDayCandleProxy[]{});

        return new FinamIntraDayCandlesProxy(intraDayCandles);
    }
}