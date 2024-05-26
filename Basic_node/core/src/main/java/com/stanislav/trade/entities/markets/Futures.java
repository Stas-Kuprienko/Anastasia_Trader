/*
 * Stanislav Kuprienko *** Omsk, Russia
 */

package com.stanislav.trade.entities.markets;

import com.stanislav.trade.entities.Board;
import com.stanislav.trade.entities.Currency;
import com.stanislav.trade.entities.Market;
import lombok.Builder;

import java.time.LocalDate;

public final class Futures implements Securities {

    private static final byte EXPIRATION_DAY_OF_MONTH = 15;

    private String ticker;
    private String name;
    private int priceStep;
    private Currency currency;
    private PriceAtTheDate price;
    private LocalDate expiration;
    private final Market market = Market.Forts;
    private final Board board = Board.FUT;


    @Builder
    public Futures(String ticker, String name, int priceStep, Currency currency, PriceAtTheDate price) {
        this.ticker = ticker;
        this.name = name;
        this.priceStep = priceStep;
        this.currency = currency;
        this.expiration = decodeExpiration(ticker);
        this.price = price;
    }

    public Futures() {}


    public static LocalDate decodeExpiration(String ticker) {
        int tickerLength = ticker.length();
        if (tickerLength < 3) {
            throw new IllegalArgumentException(ticker);
        }
        try {
            int futuresYear = Integer.parseInt(ticker.substring(tickerLength - 1));
            int year = LocalDate.now().getYear();
            for (;;) {
                if ((year % 10) < futuresYear) {
                    year += 1;
                } else if ((year % 10) > futuresYear) {
                    year -= 1;
                } else {
                    break;
                }
            }
            String futuresMonth = String.valueOf(ticker.charAt(tickerLength - 2));
            int monthValue = ExpirationMonths.valueOf(futuresMonth.toUpperCase()).ordinal() + 1;
            return LocalDate.of(year, monthValue, EXPIRATION_DAY_OF_MONTH);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(ticker);
        }
    }

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

    public LocalDate getExpiration() {
        return expiration;
    }

    public PriceAtTheDate getPrice() {
        return price;
    }

    public void setPrice(PriceAtTheDate price) {
        this.price = price;
    }

    public Market getMarket() {
        return market;
    }

    public Board getBoard() {
        return board;
    }


    enum ExpirationMonths {
        F, G, H, J, K, M, N, Q, U, V, X, Z
    }
}
