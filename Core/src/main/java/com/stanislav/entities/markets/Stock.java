package com.stanislav.entities.markets;

import lombok.Builder;
import com.stanislav.entities.Board;
import com.stanislav.entities.Currency;
import com.stanislav.entities.Market;

@Builder
public final class Stock {

    private String ticker;
    private int lotSize;
    private int priceStep;
    private Currency currency;
    private Market market;
    private Board board;
    private String issuer;


    public Stock(String ticker, int lotSize, int priceStep, Currency currency, Market market, Board board, String issuer) {
        this.ticker = ticker;
        this.lotSize = lotSize;
        this.priceStep = priceStep;
        this.currency = currency;
        this.market = market;
        this.board = board;
        this.issuer = issuer;
    }

    public Stock() {}


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

    public void setMarket(Market market) {
        this.market = market;
    }

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public String getIssuer() {
        return issuer;
    }

    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }
}