package com.stanislav.trade.domain.market.moex.converters;

import com.stanislav.trade.entities.Board;
import com.stanislav.trade.entities.Currency;
import com.stanislav.trade.entities.Market;
import com.stanislav.trade.entities.markets.Securities;
import com.stanislav.trade.entities.markets.Stock;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
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

    enum MarketData {
        SECID,
        BOARDID,
        BID,
        BIDDEPTH,
        OFFER,
        OFFERDEPTH,
        SPREAD,
        BIDDEPTHT,
        OFFERDEPTHT,
        OPEN,
        LOW,
        HIGH,
        LAST,
        LASTCHANGE,
        LASTCHANGEPRCNT,
        QTY,
        VALUE,
        VALUE_USD,
        WAPRICE,
        LASTCNGTOLASTWAPRICE,
        WAPTOPREVWAPRICEPRCNT,
        WAPTOPREVWAPRICE,
        CLOSEPRICE,
        MARKETPRICETODAY,
        MARKETPRICE,
        LASTTOPREVPRICE,
        NUMTRADES,
        VOLTODAY,
        VALTODAY,
        VALTODAY_USD,
        ETFSETTLEPRICE,
        TRADINGSTATUS,
        UPDATETIME,
        LASTBID,
        LASTOFFER,
        LCLOSEPRICE,
        LCURRENTPRICE,
        MARKETPRICE2,
        NUMBIDS,
        NUMOFFERS,
        CHANGE,
        TIME,
        HIGHBID,
        LOWOFFER,
        PRICEMINUSPREVWAPRICE,
        OPENPERIODPRICE,
        SEQNUM,
        SYSTIME,
        CLOSINGAUCTIONPRICE,
        CLOSINGAUCTIONVOLUME,
        ISSUECAPITALIZATION,
        ISSUECAPITALIZATION_UPDATETIME,
        ETFSETTLECURRENCY,
        VALTODAY_RUR,
        TRADINGSESSION,
        TRENDISSUECAPITALIZATION
    }

    public static final DateTimeFormatter DATE_TIME_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

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
            date = LocalDateTime.parse(marketData[MarketData.SYSTIME.ordinal()].toString(), DATE_TIME_FORMAT);
        } catch (DateTimeParseException e) {
            date = null;
        }
        Securities.PriceAtTheDate priceAtTheDate = new Securities.PriceAtTheDate(price, date);
        return Stock.builder()
                .ticker((String) dto[SECID.ordinal()])
                .name((String) dto[SECNAME.ordinal()])
                .currency(currency)
                .price(priceAtTheDate)
                .lotSize((Integer) dto[LOTSIZE.ordinal()])
                .dayTradeVolume(Long.parseLong(marketData[MarketData.VALTODAY.ordinal()].toString()))
                .market(Market.Stock)
                .board(Board.valueOf((String) dto[BOARDID.ordinal()]))
                .build();
    }

    public static List<Stock> moexDtoToStocks(List<Object[]> dto, List<Object[]> marketData) {
        ArrayList<Stock> stocks = new ArrayList<>();
        for (int i = 0; i < dto.size(); i++) {
            var a = dto.get(i);
            var b = marketData.get(i);
            if (a[SECID.ordinal()].equals(b[MarketData.SECID.ordinal()])) {
                stocks.add(moexDtoToStock(a, b));
            } else {
                throw new IllegalArgumentException(Arrays.toString(a) + '\n' + Arrays.toString(b));
            }
        }
        return stocks;
    }
}
