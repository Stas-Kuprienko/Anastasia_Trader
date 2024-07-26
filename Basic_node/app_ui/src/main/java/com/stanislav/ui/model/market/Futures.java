/*
 * Stanislav Kuprienko *** Omsk, Russia
 */

package com.stanislav.ui.model.market;

import com.stanislav.trade.entities.Board;
import com.stanislav.trade.entities.Currency;
import com.stanislav.trade.entities.Market;
import lombok.Builder;

import java.time.LocalDate;
import java.util.Objects;

public final class Futures implements Securities {


    private String ticker;
    private String name;
    private String asset;
    private double minStep;
    private double stepPrice;
    private Currency currency;
    private PriceAtTheDate price;
    private long dayTradeVolume;
    private LocalDate expiration;
    private Market market;
    private Board board;


    @Builder
    public Futures(String ticker, String name, String asset, double minStep, double stepPrice, Currency currency,
                   PriceAtTheDate price, long dayTradeVolume, LocalDate expiration, Market market, Board board) {
        this.ticker = ticker;
        this.name = name;
        this.asset = asset;
        this.minStep = minStep;
        this.stepPrice = stepPrice;
        this.currency = currency;
        this.price = price;
        this.dayTradeVolume = dayTradeVolume;
        this.expiration = expiration;
        this.market = market;
        this.board = board;
    }

    public Futures() {}


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

    public String getAsset() {
        return asset;
    }

    public void setAsset(String asset) {
        this.asset = asset;
    }

    public double getMinStep() {
        return minStep;
    }

    public void setMinStep(double minStep) {
        this.minStep = minStep;
    }

    public double getStepPrice() {
        return stepPrice;
    }

    public void setStepPrice(double stepPrice) {
        this.stepPrice = stepPrice;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public LocalDate getExpiration() {
        return expiration;
    }

    public void setExpiration(LocalDate expiration) {
        this.expiration = expiration;
    }

    public PriceAtTheDate getPrice() {
        return price;
    }

    public void setPrice(PriceAtTheDate price) {
        this.price = price;
    }

    public long getDayTradeVolume() {
        return dayTradeVolume;
    }

    public void setDayTradeVolume(long dayTradeVolume) {
        this.dayTradeVolume = dayTradeVolume;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Futures futures)) return false;
        return Objects.equals(ticker, futures.ticker) &&
                Objects.equals(name, futures.name) &&
                Objects.equals(asset, futures.asset) &&
                currency == futures.currency &&
                Objects.equals(expiration, futures.expiration) &&
                market == futures.market &&
                board == futures.board;
    }

    @Override
    public int hashCode() {
        return Objects.hash(ticker, name, asset, currency, expiration, market, board);
    }

    @Override
    public String toString() {
        return "Futures{" +
                "ticker='" + ticker + '\'' +
                ", name='" + name + '\'' +
                ", asset='" + asset + '\'' +
                ", minStep=" + minStep +
                ", stepPrice=" + stepPrice +
                ", currency=" + currency +
                ", price=" + price +
                ", expiration=" + expiration +
                ", market=" + market +
                ", board=" + board +
                '}';
    }

    @Override
    public int compareTo(Securities o) {
        Futures futures = (Futures) o;
        return Long.compare(futures.dayTradeVolume, dayTradeVolume);
    }
}
