package com.stanislav.trade.domain.service.moex;

import com.stanislav.trade.entities.Currency;
import com.stanislav.trade.entities.markets.Securities;
import com.stanislav.trade.entities.markets.Stock;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public enum MoexSecuritiesConverter {

    SECID,
    BOARDID,
    SHORTNAME,
    PREVPRICE,
    LOTSIZE,
    FACEVALUE,
    STATUS,
    BOARDNAME,
    DECIMALS,
    SECNAME,
    REMARKS,
    MARKETCODE,
    INSTRID,
    SECTORID,
    MINSTEP,
    PREVWAPRICE,
    FACEUNIT,
    PREVDATE,
    ISSUESIZE,
    ISIN,
    LATNAME,
    REGNUMBER,
    PREVLEGALCLOSEPRICE,
    CURRENCYID,
    SECTYPE,
    LISTLEVEL,
    SETTLEDATE,
    VALTODAY,
    LASTCHANGEPRCNT;


    public static List<Stock> moexDtoToStocks(List<Object[]> objects) {
        ArrayList<Stock> stocks = new ArrayList<>();
        for (Object[] o : objects) {
            Currency currency = o[CURRENCYID.ordinal()].equals("SUR") ?
                    Currency.RUR : Currency.valueOf((String) o[CURRENCYID.ordinal()]);
            double p = Double.parseDouble(o[PREVLEGALCLOSEPRICE.ordinal()].toString());
            LocalDate d = LocalDate.parse(o[PREVDATE.ordinal()].toString());
            Securities.PriceAtTheDate price =
                    new Securities.PriceAtTheDate(p, d);
            Stock s = Stock.builder()
                    .ticker((String) o[SECID.ordinal()])
                    .currency(currency)
                    .price(price).build();
            stocks.add(s);
        }
        return stocks;
    }
}
