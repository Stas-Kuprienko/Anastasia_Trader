package com.stanislav.trade.domain.service.moex.converters;

import com.stanislav.trade.entities.Board;
import com.stanislav.trade.entities.Currency;
import com.stanislav.trade.entities.Market;
import com.stanislav.trade.entities.markets.Futures;
import com.stanislav.trade.entities.markets.Securities;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public enum FuturesListConverter {

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
    EXERCISEFEE,
    VALTODAY,
    LASTCHANGEPRCNT;


    public static List<Futures> moexDtoToFutures(List<Object[]> dto) {
        ArrayList<Futures> futuresList = new ArrayList<>();
        for (Object[] o : dto) {
            double price = Double.parseDouble(o[PREVSETTLEPRICE.ordinal()].toString());
            LocalDate date = LocalDate.parse(o[IMTIME.ordinal()].toString().split(" ")[0]);
            LocalDate expiration = LocalDate.parse(o[LASTDELDATE.ordinal()].toString());
            Securities.PriceAtTheDate priceAtTheDate = new Securities.PriceAtTheDate(price, date);
            Futures futures = Futures.builder()
                    .ticker((String) o[SECID.ordinal()])
                    .name((String) o[SHORTNAME.ordinal()])
                    .asset((String) o[ASSETCODE.ordinal()])
                    .minStep(Double.parseDouble(o[MINSTEP.ordinal()].toString()))
                    .stepPrice(Double.parseDouble(o[STEPPRICE.ordinal()].toString()))
                    .currency(Currency.RUR)
                    .price(priceAtTheDate)
                    .expiration(expiration)
                    .board(Board.valueOf((String) o[BOARDID.ordinal()]))
                    .market(Market.Forts)
                    .build();
            futuresList.add(futures);
        }
        return futuresList;
    }
}
