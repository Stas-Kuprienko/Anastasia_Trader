/*
 * Stanislav Kuprienko *** Omsk, Russia
 */

package com.stanislav.ui.model.market;

import com.stanislav.ui.model.Board;
import com.stanislav.ui.model.ExchangeMarket;
import com.stanislav.ui.model.Market;
import lombok.Builder;
import java.util.Currency;
import java.util.Objects;

public final class Stock extends Securities {

    private int lotSize;


    @Builder
    public Stock(String ticker, String name, Currency currency, PriceAtTheDate price, int lotSize,
                 long dayTradeVolume, Market market, Board board, ExchangeMarket exchangeMarket) {
        this.ticker = ticker;
        this.name = name;
        this.currency = currency;
        this.price = price;
        this.lotSize = lotSize;
        this.dayTradeVolume = dayTradeVolume;
        this.market = market;
        this.board = board;
        this.exchangeMarket = exchangeMarket;
    }

    public Stock() {}


    public int getLotSize() {
        return lotSize;
    }

    public void setLotSize(int lotSize) {
        this.lotSize = lotSize;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Stock stock)) return false;
        return Objects.equals(ticker, stock.ticker) &&
                Objects.equals(name, stock.name) &&
                currency == stock.currency &&
                market == stock.market &&
                board == stock.board;
    }

    @Override
    public String toString() {
        return "Stock{" +
                "ticker='" + ticker + '\'' +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", lotSize=" + lotSize +
                ", currency=" + currency +
                ", market=" + market +
                ", board=" + board +
                ", exchangeMarket=" + exchangeMarket +
                '}';
    }
}