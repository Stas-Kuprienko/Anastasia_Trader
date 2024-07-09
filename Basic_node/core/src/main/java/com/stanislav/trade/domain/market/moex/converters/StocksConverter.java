package com.stanislav.trade.domain.market.moex.converters;

import com.stanislav.trade.entities.Board;
import com.stanislav.trade.entities.Currency;
import com.stanislav.trade.entities.Market;
import com.stanislav.trade.entities.markets.Securities;
import com.stanislav.trade.entities.markets.Stock;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
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

    public static Stock moexDtoToStock(Object[] dto, Object[] marketData) {
        Object value = dto[CURRENCYID.ordinal()];
        Currency currency = value != null ?
                (value.equals("SUR") ? Currency.RUR :
                        Currency.valueOf((String) dto[CURRENCYID.ordinal()]))
                : null;
        value = marketData[MarketData.LAST.ordinal()];
        double price = Double.parseDouble(value != null ? value.toString() : "0.0");
        LocalDateTime date;
        try {
            //TODO
            date = LocalDateTime.parse(marketData[MarketData.SYSTIME.ordinal()].toString(), DateTimeFormatter.ISO_DATE);
        } catch (DateTimeParseException e) {
            date = null;
        }
        Securities.PriceAtTheDate priceAtTheDate = new Securities.PriceAtTheDate(price, date);
        Stock stock = Stock.builder()
                .ticker((String) dto[SECID.ordinal()])
                .name((String) dto[SECNAME.ordinal()])
                .currency(currency)
                .price(priceAtTheDate)
                .lotSize((Integer) dto[LOTSIZE.ordinal()])
                .market(Market.Stock)
                .board(Board.valueOf((String) dto[BOARDID.ordinal()]))
                .build();
        stock.setDayTradeVolume(Long.parseLong(marketData[MarketData.VALTODAY.ordinal()].toString()));
        return stock;
    }

    public static List<Stock> moexDtoToStocks(List<Object[]> dto, List<Object[]> marketData) {
        ArrayList<Stock> stocks = new ArrayList<>();
        for (int i = 0; i < dto.size(); i++) {
            stocks.add(moexDtoToStock(dto.get(i), marketData.get(i)));
        }
        return stocks;
    }
}
