package com.stanislav.trade.domain.service.moex.converters;

import com.stanislav.trade.entities.Board;
import com.stanislav.trade.entities.Currency;
import com.stanislav.trade.entities.Market;
import com.stanislav.trade.entities.markets.Securities;
import com.stanislav.trade.entities.markets.Stock;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public enum StocksConverter {

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
    SETTLEDATE;

    public static Stock moexDtoToStock(Object[] dto) {
        Currency currency = dto[CURRENCYID.ordinal()].equals("SUR") ?
                Currency.RUR : Currency.valueOf((String) dto[CURRENCYID.ordinal()]);
        double price = Double.parseDouble(dto[PREVLEGALCLOSEPRICE.ordinal()].toString());
        LocalDate date = LocalDate.parse(dto[PREVDATE.ordinal()].toString());
        Securities.PriceAtTheDate priceAtTheDate = new Securities.PriceAtTheDate(price, date);
        return Stock.builder()
                .ticker((String) dto[SECID.ordinal()])
                .name((String) dto[SECNAME.ordinal()])
                .currency(currency)
                .price(priceAtTheDate)
                .market(Market.Stock)
                .board(Board.valueOf((String) dto[BOARDID.ordinal()]))
                .build();
    }

    public static List<Stock> moexDtoToStocks(List<Object[]> dto) {
        ArrayList<Stock> stocks = new ArrayList<>();
        for (Object[] o : dto) {
            stocks.add(moexDtoToStock(o));
        }
        return stocks;
    }
}
