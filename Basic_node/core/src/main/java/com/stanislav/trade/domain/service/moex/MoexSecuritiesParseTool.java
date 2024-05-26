package com.stanislav.trade.domain.service.moex;

import com.stanislav.trade.entities.Board;
import com.stanislav.trade.entities.Currency;
import com.stanislav.trade.entities.markets.Securities;
import com.stanislav.trade.entities.markets.Stock;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public enum MoexSecuritiesParseTool {

    BOARDID,
    TRADEDATE,
    SHORTNAME,
    SECID,
    NUMTRADES,
    VALUE,
    OPEN,
    LOW,
    HIGH,
    LEGALCLOSEPRICE,
    WAPRICE,
    CLOSE,
    VOLUME,
    MARKETPRICE2,
    MARKETPRICE3,
    ADMITTEDQUOTE,
    MP2VALTRD,
    MARKETPRICE3TRADESVALUE,
    ADMITTEDVALUE,
    WAVAL,
    TRADINGSESSION,
    CURRENCYID,
    TRENDCLSPR;


    public static List<Stock> convertMoexDtoToStocks(List<Object[]> objects) {
        ArrayList<Stock> stocks = new ArrayList<>();
        for (Object[] o : objects) {
            String board = (String) o[0];
            if (board.equalsIgnoreCase(Board.TQBR.toString())) {
                Currency currency = o[CURRENCYID.ordinal()].equals("SUR") ?
                        Currency.RUR : Currency.valueOf((String) o[CURRENCYID.ordinal()]);
                double p = Double.parseDouble(o[WAPRICE.ordinal()].toString());
                LocalDate d = LocalDate.parse(o[TRADEDATE.ordinal()].toString());
                Securities.PriceAtTheDate price =
                        new Securities.PriceAtTheDate(p, d);
                Stock s = Stock.builder()
                        .ticker((String) o[SECID.ordinal()])
                        .currency(currency)
                        .price(price).build();
                stocks.add(s);
            }
        }
        return stocks;
    }
}
