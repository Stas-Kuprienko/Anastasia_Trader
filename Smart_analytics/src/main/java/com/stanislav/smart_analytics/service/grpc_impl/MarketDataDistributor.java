package com.stanislav.smart_analytics.service.grpc_impl;

import com.google.type.Date;
import com.stanislav.smart_analytics.event_stream.grpc_impl.GRpcClient;
import grpc.tradeapi.v1.CandlesGrpc;
import org.springframework.web.bind.annotation.RequestParam;
import proto.tradeapi.v1.Candles;

import java.time.LocalDate;

public class MarketDataDistributor {

    private final CandlesGrpc.CandlesBlockingStub stub;

    public MarketDataDistributor(GRpcClient client) {
        this.stub = CandlesGrpc.newBlockingStub(client.getChannel()).withCallCredentials(client.getAuthenticator());
    }

    public void get(String ticker, String id, String timeFrame, LocalDate from, LocalDate to, Integer count) {
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

        Candles.DayCandleTimeFrame timeFrameFormat = Candles.DayCandleTimeFrame.DAYCANDLE_TIMEFRAME_D1;

        Candles.GetDayCandlesRequest request = Candles.GetDayCandlesRequest.newBuilder()
                .setSecurityCode(ticker)
                .setSecurityBoard("TQBR")
                .setInterval(interval)
                .setTimeFrame(timeFrameFormat)
                .build();

        Candles.GetDayCandlesResult result = stub.getDayCandles(request);
        //TODO ?????????????????
    }
}
