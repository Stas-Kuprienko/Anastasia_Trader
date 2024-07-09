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
    private String name;
    private PriceAtTheDate price;
    private int lotSize;
    private long dayTradeVolume;
    private Currency currency;
    private Market market;
    private Board board;


    @Builder
    public Stock(String ticker, String name, Currency currency, PriceAtTheDate price, int lotSize, long dayTradeVolume, Market market, Board board) {
        this.ticker = ticker;
        this.name = name;
        this.currency = currency;
        this.price = price;
        this.lotSize = lotSize;
        this.dayTradeVolume = dayTradeVolume;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLotSize() {
        return lotSize;
    }

    public void setLotSize(int lotSize) {
        this.lotSize = lotSize;
    }

    public long getDayTradeVolume() {
        return dayTradeVolume;
    }

    public void setDayTradeVolume(long dayTradeVolume) {
        this.dayTradeVolume = dayTradeVolume;
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

    public void setMarket(Market market) {
        this.market = market;
    }

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
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
        return lotSize == stock.lotSize &&
                Objects.equals(ticker, stock.ticker) &&
                Objects.equals(name, stock.name) &&
                currency == stock.currency &&
                market == stock.market &&
                board == stock.board;
    }

    @Override
    public int hashCode() {
        return Objects.hash(ticker, name, lotSize, currency, market, board);
    }

    @Override
    public String toString() {
        return "Stock{" +
                "ticker='" + ticker + '\'' +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", lotSize=" + lotSize +
                ", dayTradeVolume=" + dayTradeVolume +
                ", currency=" + currency +
                ", market=" + market +
                ", board=" + board +
                '}';
    }

    @Override
    public int compareTo(Long o) {
        return Long.compare(dayTradeVolume, o);
    }
}