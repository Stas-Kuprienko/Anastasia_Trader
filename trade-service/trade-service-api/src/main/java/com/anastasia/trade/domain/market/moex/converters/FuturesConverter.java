package com.anastasia.trade.domain.market.moex.converters;

import com.anastasia.trade.entities.Board;
import com.anastasia.trade.entities.Currency;
import com.anastasia.trade.entities.ExchangeMarket;
import com.anastasia.trade.entities.Market;
import com.anastasia.trade.entities.markets.Futures;
import com.anastasia.trade.entities.markets.Securities;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum FuturesConverter {

    SECID,
    BOARDID,
    SHORTNAME,
    SECNAME,
    PREVSETTLEPRICE,
    DECIMALS,
    MINSTEP,
    LASTTRADEDATE,
    LASTDELDATE,
    SECTYPE,
    LATNAME,
    ASSETCODE,
    PREVOPENPOSITION,
    LOTVOLUME,
    INITIALMARGIN,
    HIGHLIMIT,
    LOWLIMIT,
    STEPPRICE,
    LASTSETTLEPRICE,
    PREVPRICE,
    IMTIME,
    BUYSELLFEE,
    SCALPERFEE,
    NEGOTIATEDFEE,
    EXERCISEFEE;

    enum MarketData {
        SECID,
        BOARDID,
        BID,
        OFFER,
        SPREAD,
        OPEN,
        HIGH,
        LOW,
        LAST,
        QUANTITY,
        LASTCHANGE,
        SETTLEPRICE,
        SETTLETOPREVSETTLE,
        OPENPOSITION,
        NUMTRADES,
        VOLTODAY,
        VALTODAY,
        VALTODAY_USD,
        UPDATETIME,
        LASTCHANGEPRCNT,
        BIDDEPTH,
        BIDDEPTHT,
        NUMBIDS,
        OFFERDEPTH,
        OFFERDEPTHT,
        NUMOFFERS,
        TIME,
        SETTLETOPREVSETTLEPRC,
        SEQNUM,
        SYSTIME,
        TRADEDATE,
        LASTTOPREVPRICE,
        OICHANGE,
        OPENPERIODPRICE,
        SWAPRATE
    }

    public static final DateTimeFormatter DATE_TIME_FORMAT = StocksConverter.DATE_TIME_FORMAT;

    public static Futures moexDtoToFutures(Object[] dto, Object[] marketData) {
        double price = Double.parseDouble(marketData[MarketData.LAST.ordinal()].toString());
        LocalDateTime date = LocalDateTime.parse(marketData[MarketData.SYSTIME.ordinal()].toString(), DATE_TIME_FORMAT);
        LocalDate expiration = LocalDate.parse(dto[LASTDELDATE.ordinal()].toString());
        Securities.PriceAtTheDate priceAtTheDate = new Securities.PriceAtTheDate(price, date);
        return Futures.builder()
                .ticker((String) dto[SECID.ordinal()])
                .name((String) dto[SHORTNAME.ordinal()])
                .asset((String) dto[ASSETCODE.ordinal()])
                .minStep(Double.parseDouble(dto[MINSTEP.ordinal()].toString()))
                .stepPrice(Double.parseDouble(dto[STEPPRICE.ordinal()].toString()))
                .currency(Currency.RUR)
                .price(priceAtTheDate)
                .dayTradeVolume(Long.parseLong(marketData[MarketData.VALTODAY.ordinal()].toString()))
                .expiration(expiration)
                .board(Board.valueOf((String) dto[BOARDID.ordinal()]))
                .market(Market.Forts)
                .exchangeMarket(ExchangeMarket.MOEX)
                .build();
    }

    public static List<Futures> moexDtoToFuturesList(List<Object[]> dto, List<Object[]> marketData) {
        ArrayList<Futures> futuresList = new ArrayList<>();
        for (int i = 0; i < dto.size(); i++) {
            var a = dto.get(i);
            var b = marketData.get(i);
            if (a[SECID.ordinal()].equals(b[MarketData.SECID.ordinal()])) {
                futuresList.add(moexDtoToFutures(dto.get(i), marketData.get(i)));
            } else {
                throw new IllegalArgumentException(Arrays.toString(a) + '\n' + Arrays.toString(b));
            }
        }
        return futuresList;
    }
}
