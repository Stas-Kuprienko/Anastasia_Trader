/*
 * Stanislav Kuprienko *** Omsk, Russia
 */

package com.anastasia.trade.domain.market.finam.dto;

import com.anastasia.trade.entities.Currency;
import com.anastasia.trade.entities.Market;
import com.anastasia.trade.entities.markets.Stock;
import lombok.Builder;

@Builder
public final class FinamSecuritiesResponseDto {

    private String code;
    private String board;
    private Market market;
    private int decimals;
    private int lotSize;
    private int minStep;
    private String currency;
    private String shortName;
    private int properties;
    private String timeZoneName;
    private int bpCost;
    private int accruedInterest;
    private PriceSign priceSign;
    private String ticker;
    private int lotDivider;


    public FinamSecuritiesResponseDto(String code, String board, Market market,
                                      int decimals, int lotSize, int minStep,
                                      String currency, String shortName, int properties,
                                      String timeZoneName, int bpCost, int accruedInterest,
                                      PriceSign priceSign, String ticker, int lotDivider) {
        this.code = code;
        this.board = board;
        this.market = market;
        this.decimals = decimals;
        this.lotSize = lotSize;
        this.minStep = minStep;
        this.currency = currency;
        this.shortName = shortName;
        this.properties = properties;
        this.timeZoneName = timeZoneName;
        this.bpCost = bpCost;
        this.accruedInterest = accruedInterest;
        this.priceSign = priceSign;
        this.ticker = ticker;
        this.lotDivider = lotDivider;
    }

    public FinamSecuritiesResponseDto() {}


    public Stock toStockClass() {
        return Stock.builder()
                .ticker(code)
                .currency(Currency.valueOf(currency))
                .build();
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getBoard() {
        return board;
    }

    public void setBoard(String board) {
        this.board = board;
    }

    public Market getMarket() {
        return market;
    }

    public void setMarket(Market market) {
        this.market = market;
    }

    public int getDecimals() {
        return decimals;
    }

    public void setDecimals(int decimals) {
        this.decimals = decimals;
    }

    public int getLotSize() {
        return lotSize;
    }

    public void setLotSize(int lotSize) {
        this.lotSize = lotSize;
    }

    public int getMinStep() {
        return minStep;
    }

    public void setMinStep(int minStep) {
        this.minStep = minStep;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public int getProperties() {
        return properties;
    }

    public void setProperties(int properties) {
        this.properties = properties;
    }

    public String getTimeZoneName() {
        return timeZoneName;
    }

    public void setTimeZoneName(String timeZoneName) {
        this.timeZoneName = timeZoneName;
    }

    public int getBpCost() {
        return bpCost;
    }

    public void setBpCost(int bpCost) {
        this.bpCost = bpCost;
    }

    public int getAccruedInterest() {
        return accruedInterest;
    }

    public void setAccruedInterest(int accruedInterest) {
        this.accruedInterest = accruedInterest;
    }

    public PriceSign getPriceSign() {
        return priceSign;
    }

    public void setPriceSign(PriceSign priceSign) {
        this.priceSign = priceSign;
    }

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public int getLotDivider() {
        return lotDivider;
    }

    public void setLotDivider(int lotDivider) {
        this.lotDivider = lotDivider;
    }


    enum PriceSign {

        Positive, NonNegative, Any
    }

    @Override
    public String toString() {
        return "FinamSecuritiesResponseDto{" +
                "code='" + code + '\'' +
                ", board='" + board + '\'' +
                ", market=" + market +
                ", decimals=" + decimals +
                ", lotSize=" + lotSize +
                ", minStep=" + minStep +
                ", currency='" + currency + '\'' +
                ", shortName='" + shortName + '\'' +
                ", properties=" + properties +
                ", timeZoneName='" + timeZoneName + '\'' +
                ", bpCost=" + bpCost +
                ", accruedInterest=" + accruedInterest +
                ", priceSign=" + priceSign +
                ", ticker='" + ticker + '\'' +
                ", lotDivider=" + lotDivider +
                '}';
    }
}
