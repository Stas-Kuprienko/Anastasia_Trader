package com.stanislav.smart_analytics.domain.market.finam;

import com.google.type.Date;
import com.stanislav.smart_analytics.domain.entities.Board;
import com.stanislav.smart_analytics.domain.entities.TimeFrame;
import com.stanislav.smart_analytics.domain.entities.candles.DayCandles;
import com.stanislav.smart_analytics.domain.entities.candles.IntraDayCandles;
import com.stanislav.smart_analytics.domain.event_stream.grpc_impl.GRpcClient;
import com.stanislav.smart_analytics.domain.market.MarketDataProvider;
import grpc.tradeapi.v1.CandlesGrpc;
import proto.tradeapi.v1.Candles;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class FinamGRpcMarketDataProvider implements MarketDataProvider {

    private final CandlesGrpc.CandlesBlockingStub stub;


    public FinamGRpcMarketDataProvider(GRpcClient client) {
        this.stub = CandlesGrpc.newBlockingStub(client.getChannel())
                .withCallCredentials(client.getAuthenticator());
    }


    @Override
    public DayCandles getDayCandles(String ticker, Board board, TimeFrame.Day timeFrame,
                                    LocalDate from, LocalDate to, Integer count) {
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
                .setTo(dateTo)
                .setCount(count).build();

        Candles.GetDayCandlesRequest request = Candles.GetDayCandlesRequest.newBuilder()
                .setSecurityCode(ticker)
                .setSecurityBoard(board.name())
                .setInterval(interval)
                .setTimeFrame(TimeFrame.toProtoValue(timeFrame))
                .build();

        Candles.GetDayCandlesResult result = stub.getDayCandles(request);

        FinamDayCandlesProxy.FinamDayCandleProxy[] dayCandles = result
                        .getCandlesList()
                        .stream()
                        .map(FinamDayCandlesProxy.FinamDayCandleProxy::new)
                        .toList()
                        .toArray(new FinamDayCandlesProxy.FinamDayCandleProxy[]{});

        return new FinamDayCandlesProxy(dayCandles);
    }

    @Override
    public DayCandles getDayCandles(String ticker, Board board,
                                    TimeFrame.Day timeFrame, LocalDate from, LocalDate to) {
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

        Candles.GetDayCandlesRequest request = Candles.GetDayCandlesRequest.newBuilder()
                .setSecurityCode(ticker)
                .setSecurityBoard(board.name())
                .setInterval(interval)
                .setTimeFrame(TimeFrame.toProtoValue(timeFrame))
                .build();

        Candles.GetDayCandlesResult result = stub.getDayCandles(request);

        FinamDayCandlesProxy.FinamDayCandleProxy[] dayCandles =
                (FinamDayCandlesProxy.FinamDayCandleProxy[]) result
                        .getCandlesList()
                        .stream()
                        .map(FinamDayCandlesProxy.FinamDayCandleProxy::new)
                        .toArray();

        return new FinamDayCandlesProxy(dayCandles);
    }

    @Override
    public IntraDayCandles getIntraDayCandles(String ticker, Board board, TimeFrame.IntraDay timeFrame,
                                              LocalDateTime from, LocalDateTime to, Integer count) {
        return null;
    }

    @Override
    public IntraDayCandles getIntraDayCandles(String ticker, Board board,
                                              TimeFrame.IntraDay timeFrame, LocalDateTime from, LocalDateTime to) {
        return null;
    }
}
