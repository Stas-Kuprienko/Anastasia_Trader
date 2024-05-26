/*
 * Stanislav Kuprienko *** Omsk, Russia
 */

package com.stanislav.trade.entities.markets;

import lombok.Builder;
import com.stanislav.trade.entities.Board;
import com.stanislav.trade.entities.Currency;
import com.stanislav.trade.entities.Market;

import java.util.Objects;

public final class Stock implements Securities {

    private String ticker;
    private Currency currency;
    private PriceAtTheDate price;
    private final Market market = Market.Stock;
    private final Board board = Board.TQBR;


    @Builder
    public Stock(String ticker, Currency currency, PriceAtTheDate price) {
        this.ticker = ticker;
        this.currency = currency;
        this.price = price;
    }

    public Stock() {}


    public static Stock emptyStock() {
        return new Stock("incorrect", null, null);
    }

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public Market getMarket() {
        return market;
    }

    public Board getBoard() {
        return board;
    }

    public PriceAtTheDate getPrice() {
        return price;
    }

    public void setPrice(PriceAtTheDate price) {
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Stock stock)) return false;
        return Objects.equals(ticker, stock.ticker);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(ticker);
    }

    @Override
    public String toString() {
        return "Stock{" +
                "ticker='" + ticker +
                "', " + price +
                ", currency=" + currency +
                '}';
    }
}