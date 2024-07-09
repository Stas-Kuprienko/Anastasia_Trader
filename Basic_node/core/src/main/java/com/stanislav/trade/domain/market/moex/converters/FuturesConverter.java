package com.stanislav.trade.domain.market.moex.converters;

import com.stanislav.trade.entities.Board;
import com.stanislav.trade.entities.Currency;
import com.stanislav.trade.entities.Market;
import com.stanislav.trade.entities.markets.Futures;
import com.stanislav.trade.entities.markets.Securities;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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

    public static Futures moexDtoToFutures(Object[] dto, Object[] marketData) {
        double price = Double.parseDouble(marketData[MarketData.LAST.ordinal()].toString());
        LocalDateTime date = LocalDateTime.parse(marketData[MarketData.SYSTIME.ordinal()].toString(), DateTimeFormatter.ISO_DATE);
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
                .dayTradeVolume((Integer) marketData[MarketData.VALTODAY.ordinal()])
                .expiration(expiration)
                .board(Board.valueOf((String) dto[BOARDID.ordinal()]))
                .market(Market.Forts)
                .build();
    }

    public static List<Futures> moexDtoToFuturesList(List<Object[]> dto, List<Object[]> marketData) {
        ArrayList<Futures> futuresList = new ArrayList<>();
        for (int i = 0; i < dto.size(); i++) {
            futuresList.add(moexDtoToFutures(dto.get(i), marketData.get(i)));
        }
        return futuresList;
    }
}
