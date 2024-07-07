package com.stanislav.smart.domain.market.finam;

import com.google.protobuf.Timestamp;
import com.google.type.Date;
import com.stanislav.smart.domain.entities.Board;
import com.stanislav.smart.domain.entities.TimeFrame;
import com.stanislav.smart.domain.entities.candles.DayCandleBox;
import com.stanislav.smart.domain.entities.candles.IntraDayCandleBox;
import com.stanislav.smart.domain.entities.candles.PriceCandleBox;
import com.stanislav.smart.domain.market.MarketDataProvider;
import com.stanislav.smart.domain.market.finam.candles.FinamDayCandleBoxProxy;
import com.stanislav.smart.domain.market.finam.candles.FinamDayCandleProxy;
import com.stanislav.smart.domain.market.finam.candles.FinamIntraDayCandleBoxProxy;
import com.stanislav.smart.domain.market.finam.candles.FinamIntraDayCandleProxy;
import com.stanislav.smart.service.grpc_impl.GRpcClient;
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
    public PriceCandleBox getLastNumberOfCandles(String ticker, Board board, TimeFrame.Scope timeFrame, Integer count) {

        if (TimeFrame.Day.class.equals(timeFrame.getClass())) {
            return getDayCandles(ticker, board, timeFrame, LocalDate.now(), count);

        } else if (TimeFrame.IntraDay.class.equals(timeFrame.getClass())) {
            return getIntraDayCandles(ticker, board, timeFrame, LocalDateTime.now(), count);

        } else {
            throw new IllegalArgumentException("time=" + timeFrame);
        }
    }

    @Override
    public DayCandleBox getDayCandles(String ticker, Board board, TimeFrame.Scope timeFrame, LocalDate to, Integer count) {
        try {
            return switch (board) {
                case TQBR -> getStockDayCandles(ticker, (TimeFrame.Day) timeFrame, to, count);
                case FUT -> getFuturesDayCandles(ticker, (TimeFrame.Day) timeFrame, to, count);
                case null, default -> throw new IllegalArgumentException("board=" + board);
            };
        } catch (ClassCastException e) {
            //TODO logger
            throw new IllegalArgumentException("time_frame=" + timeFrame);
        }
    }

    @Override
    public DayCandleBox getDayCandles(String ticker, Board board, TimeFrame.Scope timeFrame, LocalDate from, LocalDate to) {
        try {
            return switch (board) {
                case TQBR -> getStockDayCandles(ticker, (TimeFrame.Day) timeFrame, from, to);
                case FUT -> getFuturesDayCandles(ticker, (TimeFrame.Day) timeFrame, from, to);
                case null, default -> throw new IllegalArgumentException("board=" + board);
            };
        } catch (ClassCastException e) {
            //TODO logger
            throw new IllegalArgumentException("time_frame=" + timeFrame);
        }
    }

    @Override
    public IntraDayCandleBox getIntraDayCandles(String ticker, Board board, TimeFrame.Scope timeFrame, LocalDateTime to, Integer count) {
        try {
            return switch (board) {
                case TQBR -> getStockIntraDayCandles(ticker, (TimeFrame.IntraDay) timeFrame, to, count);
                case FUT -> getFuturesIntraDayCandles(ticker, (TimeFrame.IntraDay) timeFrame, to, count);
                case null, default -> throw new IllegalArgumentException("board=" + board);
            };
        } catch (ClassCastException e) {
            //TODO logger
            throw new IllegalArgumentException("time_frame=" + timeFrame);
        }
    }

    @Override
    public IntraDayCandleBox getIntraDayCandles(String ticker, Board board, TimeFrame.Scope timeFrame, LocalDateTime from, LocalDateTime to) {
        try {
            return switch (board) {
                case TQBR -> getStockIntraDayCandles(ticker, (TimeFrame.IntraDay) timeFrame, from, to);
                case FUT -> getFuturesIntraDayCandles(ticker, (TimeFrame.IntraDay) timeFrame, from, to);
                case null, default -> throw new IllegalArgumentException("board=" + board);
            };
        } catch (ClassCastException e) {
            //TODO logger
            throw new IllegalArgumentException("time_frame=" + timeFrame);
        }
    }


    @Override
    public DayCandleBox getStockDayCandles(String ticker, TimeFrame.Day timeFrame, LocalDate to, Integer count) {
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
    public DayCandleBox getStockDayCandles(String ticker, TimeFrame.Day timeFrame, LocalDate from, LocalDate to) {
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
    public IntraDayCandleBox getStockIntraDayCandles(String ticker, TimeFrame.IntraDay timeFrame,
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
    public IntraDayCandleBox getStockIntraDayCandles(String ticker, TimeFrame.IntraDay timeFrame,
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
    public DayCandleBox getFuturesDayCandles(String ticker, TimeFrame.Day timeFrame, LocalDate to, Integer count) {
        //TODO
        getDayCandlesResult(ticker, Board.FUT, timeFrame, null);
        return null;
    }

    @Override
    public DayCandleBox getFuturesDayCandles(String ticker, TimeFrame.Day timeFrame, LocalDate from, LocalDate to) {
        return null;
    }

    @Override
    public IntraDayCandleBox getFuturesIntraDayCandles(String ticker, TimeFrame.IntraDay timeFrame,
                                                    LocalDateTime to, Integer count) {
        return null;
    }

    @Override
    public IntraDayCandleBox getFuturesIntraDayCandles(String ticker, TimeFrame.IntraDay timeFrame,
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

    private FinamDayCandleBoxProxy buildDayCandlesProxy(Candles.GetDayCandlesResult result) {
        return new FinamDayCandleBoxProxy(result
                .getCandlesList()
                .stream()
                .map(FinamDayCandleProxy::new)
                .toList());
    }

    private FinamIntraDayCandleBoxProxy buildIntraDayCandlesProxy(Candles.GetIntradayCandlesResult result) {
        return new FinamIntraDayCandleBoxProxy(result
                .getCandlesList()
                .stream().map(FinamIntraDayCandleProxy::new)
                .toList());
    }
}