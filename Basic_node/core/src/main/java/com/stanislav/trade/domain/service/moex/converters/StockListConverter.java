package com.stanislav.trade.domain.service.moex.converters;

import com.stanislav.trade.entities.Board;
import com.stanislav.trade.entities.Currency;
import com.stanislav.trade.entities.Market;
import com.stanislav.trade.entities.markets.Securities;
import com.stanislav.trade.entities.markets.Stock;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public enum StockListConverter {

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


    public static List<Stock> moexDtoToStocks(List<Object[]> dto) {
        ArrayList<Stock> stocks = new ArrayList<>();
        for (Object[] o : dto) {
            Currency currency = o[CURRENCYID.ordinal()].equals("SUR") ?
                    Currency.RUR : Currency.valueOf((String) o[CURRENCYID.ordinal()]);
            double price = Double.parseDouble(o[PREVLEGALCLOSEPRICE.ordinal()].toString());
            LocalDate date = LocalDate.parse(o[PREVDATE.ordinal()].toString());
            Securities.PriceAtTheDate priceAtTheDate = new Securities.PriceAtTheDate(price, date);
            Stock s = Stock.builder()
                    .ticker((String) o[SECID.ordinal()])
                    .currency(currency)
                    .price(priceAtTheDate)
                    .market(Market.Stock)
                    .board(Board.valueOf((String) o[BOARDID.ordinal()]))
                    .build();
            stocks.add(s);
        }
        return stocks;
    }
}
