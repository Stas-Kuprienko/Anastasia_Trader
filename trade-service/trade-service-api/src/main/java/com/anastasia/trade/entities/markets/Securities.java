/*
 * Stanislav Kuprienko *** Omsk, Russia
 */

package com.anastasia.trade.entities.markets;

import com.anastasia.trade.entities.Board;
import com.anastasia.trade.entities.Currency;
import com.anastasia.trade.entities.ExchangeMarket;
import com.anastasia.trade.entities.Market;

import java.io.Serializable;
import java.time.LocalDateTime;

public abstract class Securities implements Serializable, Comparable<Securities> {

    protected String ticker;
    protected String name;
    protected PriceAtTheDate price;
    protected long dayTradeVolume;
    protected Currency currency;
    protected Market market;
    protected Board board;
    protected ExchangeMarket exchangeMarket;


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

    public ExchangeMarket getExchangeMarket() {
        return exchangeMarket;
    }

    public void setExchangeMarket(ExchangeMarket exchangeMarket) {
        this.exchangeMarket = exchangeMarket;
    }

    @Override
    public int compareTo(Securities o) {
        return Long.compare(o.dayTradeVolume, dayTradeVolume);
    }

    public record PriceAtTheDate(double price, LocalDateTime time) implements Serializable {}
}
