package com.stanislav.entities.markets;

import lombok.Builder;
import com.stanislav.entities.Board;
import com.stanislav.entities.Currency;
import com.stanislav.entities.Market;

import java.util.Objects;

public final class Stock implements Securities {

    private String ticker;
    private int lotSize;
    private int priceStep;
    private Currency currency;
    private PriceAtTheTime price;
    private final Market market = Market.Stock;
    private final Board board = Board.TQBR;


    @Builder
    public Stock(String ticker, int lotSize, int priceStep, Currency currency, PriceAtTheTime price) {
        this.ticker = ticker;
        this.lotSize = lotSize;
        this.priceStep = priceStep;
        this.currency = currency;
        this.price = price;
    }

    public Stock() {}


    public static Stock emptyStock() {
        return new Stock("incorrect", 0, 0, null, null);
    }

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public int getLotSize() {
        return lotSize;
    }

    public void setLotSize(int lotSize) {
        this.lotSize = lotSize;
    }

    public int getPriceStep() {
        return priceStep;
    }

    public void setPriceStep(int priceStep) {
        this.priceStep = priceStep;
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

    public PriceAtTheTime getPrice() {
        return price;
    }

    public void setPrice(PriceAtTheTime price) {
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
                "price=" + price +
                ", currency=" + currency +
                ", priceStep=" + priceStep +
                ", lotSize=" + lotSize +
                ", ticker='" + ticker + '\'' +
                '}';
    }
}