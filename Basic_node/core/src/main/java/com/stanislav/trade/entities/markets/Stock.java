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
    private Market market;
    private Board board;


    @Builder
    public Stock(String ticker, Currency currency, PriceAtTheDate price, Market market, Board board) {
        this.ticker = ticker;
        this.currency = currency;
        this.price = price;
        this.market = market;
        this.board = board;
    }

    public Stock() {}


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
        return Objects.equals(ticker, stock.ticker) &&
                currency == stock.currency &&
                market == stock.market &&
                board == stock.board;
    }

    @Override
    public int hashCode() {
        return Objects.hash(ticker, currency, market, board);
    }

    @Override
    public String toString() {
        return "Stock{" +
                "ticker='" + ticker + '\'' +
                ", currency=" + currency +
                ", price=" + price +
                ", market=" + market +
                ", board=" + board +
                '}';
    }
}